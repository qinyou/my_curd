package com.github.qinyou.oa.controller;

import com.github.qinyou.common.annotation.RequirePermission;
import com.github.qinyou.common.render.ZipRender;
import com.github.qinyou.common.utils.FileUtils;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.common.utils.WebUtils;
import com.github.qinyou.common.validator.IdRequired;
import com.github.qinyou.common.validator.IdsRequired;
import com.github.qinyou.common.web.BaseController;
import com.github.qinyou.oa.activiti.ActivitiKit;
import com.github.qinyou.oa.vo.ProcessDeploymentInfo;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentQuery;
import org.apache.commons.io.FilenameUtils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * 流程部署管理
 *
 * @author chuang
 */
@Slf4j
@RequirePermission("processDeploy")
public class ProcessDeployController extends BaseController {

    public void index() {
        render("oa/processDeploy.ftl");
    }

    // 部署数据
    public void query() {
        int pageNumber = getParaToInt("page", 1);
        int pageSize = getParaToInt("rows", 30);
        String category = get("extra_category");
        String deploymentName = get("extra_name");

        DeploymentQuery query = ActivitiKit.getRepositoryService().createDeploymentQuery();
        if (StringUtils.notEmpty(category)) {
            query.deploymentCategory(category);
        }
        if (StringUtils.notEmpty(deploymentName)) {
            query.deploymentNameLike("%" + deploymentName + "%");
        }

        List<ProcessDeploymentInfo> list = new ArrayList<>();
        query.orderByDeploymenTime().desc().listPage((pageNumber - 1) * pageSize, pageSize).forEach(deployment -> {
            list.add(new ProcessDeploymentInfo()
                    .setId(deployment.getId())
                    .setName(deployment.getName())
                    .setCategory(deployment.getCategory())
                    .setDeploymentTime(deployment.getDeploymentTime()));
        });
        Long total = query.count();
        renderDatagrid(list, total.intValue());
    }

    // 部署弹窗
    public void deployModel() {
        render("oa/processDeploy_form.ftl");
    }

    // 部署操作
    public void deployAction() {
        UploadFile file = getFile();
        if (file == null) {
            renderFail("部署失败");
            return;
        }
        String extension = FilenameUtils.getExtension(file.getFileName());
        if (!"zip".equalsIgnoreCase(extension)) {
            renderFail("部署包必须是zip压缩文件");
            return;
        }
        String category = get("category");
        String name = get("name");
        if (StringUtils.isEmpty(category) || StringUtils.isEmpty(name)) {
            renderFail("参数缺失");
            return;
        }

        try (InputStream in = new FileInputStream(file.getFile())) {
            ZipInputStream zipInputStream = new ZipInputStream(in);
            Deployment deployment = ActivitiKit.getRepositoryService()
                    .createDeployment().addZipInputStream(zipInputStream)
                    .category(category)
                    .name(name)
                    .deploy();

            log.info("{} 部署流程, deploymentId:{}, deploymentName:{}, deploymentCategory:{}",
                    WebUtils.getSessionUsername(this), deployment.getId(), deployment.getName(), deployment.getCategory());

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        FileUtils.deleteFile(file.getFile());

        renderSuccess("部署成功");
    }

    // 卸载 操作
    @Before({IdsRequired.class, Tx.class})
    public void unDeployAction() {
        boolean cascade = getParaToBoolean("cascade", true);
        RepositoryService service = ActivitiKit.getRepositoryService();
        for (String id : getPara("ids").split(",")) {
            // cascade 如为 false, 如果 流程已经启动，抛出 runtime exception 触发事务
            log.info("{} 部署流程, deploymentId:{}, cascade:{}", WebUtils.getSessionUsername(this), id, cascade);
            service.deleteDeployment(id, cascade);
        }
        renderSuccess("卸载成功");
    }


    /**
     * 下载部署包
     */
    @Before(IdRequired.class)
    public void downloadZip() {
        String deploymentId = getPara("id");
        Deployment deployment = ActivitiKit.getRepositoryService().createDeploymentQuery()
                .deploymentId(deploymentId)
                .singleResult();
        if (deployment == null) {
            renderFail("部署包不存在");
            return;
        }

        List<String> resourceNames = ActivitiKit.getRepositoryService().getDeploymentResourceNames(deploymentId);
        List<InputStream> resourceDatas = new ArrayList<>();

        for (String resourceName : resourceNames) {
            resourceDatas.add(ActivitiKit.getRepositoryService().getResourceAsStream(deploymentId, resourceName));
        }
        render(ZipRender.me().filenames(resourceNames).dataIn(resourceDatas).fileName("部署包[" + deployment.getId() + "].zip"));
    }

}
