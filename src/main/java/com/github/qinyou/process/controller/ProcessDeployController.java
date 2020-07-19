package com.github.qinyou.process.controller;

import com.github.qinyou.common.activiti.ActivitiUtils;
import com.github.qinyou.common.annotation.RequirePermission;
import com.github.qinyou.common.render.ZipRender;
import com.github.qinyou.common.utils.FileUtils;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.common.validator.IdRequired;
import com.github.qinyou.common.validator.IdsRequired;
import com.github.qinyou.common.web.BaseController;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentQuery;
import org.apache.commons.io.FilenameUtils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        render("process/processDeploy.ftl");
    }

    // 部署数据
    public void query() {
        int pageNumber = getParaToInt("page", 1);
        int pageSize = getParaToInt("rows", 30);

        String id = get("extra_id");
        String name = get("extra_name");
        DeploymentQuery query = ActivitiUtils.getRepositoryService().createDeploymentQuery();
        if (StringUtils.notEmpty(id)) {
            query.deploymentId(id);
        }
        if (StringUtils.notEmpty(name)) {
            query.deploymentNameLike("%" + name + "%");
        }

        List<Map<String, Object>> list = new ArrayList<>();
        query.orderByDeploymenTime().desc().listPage((pageNumber - 1) * pageSize, pageSize).forEach(deployment -> {
            Map<String, Object> item = new HashMap<>();
            item.put("id", deployment.getId());
            item.put("name", deployment.getName());
            item.put("date", deployment.getDeploymentTime());
            list.add(item);
        });
        Long total = query.count();
        renderDatagrid(list, total.intValue());
    }

    // 部署弹窗
    public void deployModel() {
        render("process/processDeploy_form.ftl");
    }

    // 部署操作
    public void deployAction() {
        UploadFile file = getFile();
        if (file == null) {
            renderFail("上传文件不可为空");
            return;
        }
        String extension = FilenameUtils.getExtension(file.getFileName());
        if (!"zip".equalsIgnoreCase(extension)) {
            renderFail("部署包必须是zip压缩文件");
            return;
        }
        String name = get("name");         // 备注
        if (StringUtils.isEmpty(name)) {
            renderFail("部署名不可为空");
            return;
        }

        try (InputStream in = new FileInputStream(file.getFile())) {
            ZipInputStream zipInputStream = new ZipInputStream(in);
            ActivitiUtils.getRepositoryService()
                    .createDeployment().addZipInputStream(zipInputStream)
                    .name(name)
                    .deploy();
            FileUtils.deleteFile(file.getFile());
            renderSuccess("部署成功");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            FileUtils.deleteFile(file.getFile());
            renderFail("部署异常：" + e.getMessage());
        }
    }

    // 卸载 操作
    @Before({IdsRequired.class, Tx.class})
    public void unDeployAction() {
        for (String id : getPara("ids").split(",")) {
            // cascade 为true，如流程已启动，强制删除流程实例
            //         如为 false, 如流程已启动，抛出 runtime exception 触发事务
            ActivitiUtils.getRepositoryService().deleteDeployment(id, true);
        }
        renderSuccess("卸载成功");
    }


    /**
     * 下载部署包
     */
    @Before(IdRequired.class)
    public void downloadZip() {
        String deploymentId = get("id");
        Deployment deployment = ActivitiUtils.getRepositoryService()
                .createDeploymentQuery()
                .deploymentId(deploymentId)
                .singleResult();
        if (deployment == null) {
            renderFail("部署包不存在");
            return;
        }

        List<String> resourceNames = ActivitiUtils.getRepositoryService().getDeploymentResourceNames(deploymentId);
        List<InputStream> resourceDatas = new ArrayList<>();

        for (String resourceName : resourceNames) {
            resourceDatas.add(ActivitiUtils.getRepositoryService().getResourceAsStream(deploymentId, resourceName));
        }

        render(ZipRender.me().filenames(resourceNames).dataIn(resourceDatas).fileName("部署包[id-" + deployment.getId() + "].zip"));
    }

}
