package com.github.qinyou.process.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.qinyou.common.activiti.ActConst;
import com.github.qinyou.common.activiti.ActivitiUtils;
import com.github.qinyou.common.annotation.RequirePermission;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.common.validator.IdRequired;
import com.github.qinyou.common.validator.IdsRequired;
import com.github.qinyou.common.web.BaseController;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模型编辑器
 */
@SuppressWarnings("Duplicates")
@Slf4j
@RequirePermission("processModel")
public class ProcessModelController extends BaseController {
    public void index() {
        render("process/processModel.ftl");
    }

    // 表格数据
    public void query() {
        int pageNumber = getInt("page", 1);
        int pageSize = getInt("rows", 30);

        ModelQuery query = ActivitiUtils.getRepositoryService().createModelQuery();
        String name = get("extra_name");
        if (StringUtils.notEmpty(name)) {
            query.modelNameLike("%" + name + "%");
        }
        String id = get("extra_id");
        if (StringUtils.notEmpty(id)) {
            query.modelId(id);
        }
        List<Map<String, Object>> list = new ArrayList<>();
        query.orderByModelKey().orderByLastUpdateTime().desc()
                .listPage((pageNumber - 1) * pageSize, pageSize)
                .forEach(model -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", model.getId());
                    item.put("name", model.getName());
                    item.put("description", JSON.parseObject(model.getMetaInfo()).get("description"));
                    item.put("createTime", model.getCreateTime());
                    item.put("lastUpdateTime", model.getLastUpdateTime());
                    list.add(item);
                });
        Long total = query.count();
        renderDatagrid(list, total.intValue());
    }

    // 删除模型
    @Before(IdsRequired.class)
    public void deleteAction() {
        String ids = getPara("ids");
        RepositoryService service = ActivitiUtils.getRepositoryService();
        for (String id : ids.split(",")) {
            service.deleteModel(id);
        }
        renderSuccess("删除成功");
    }


    // 新增模型
    @Before(Tx.class)
    public void newModel() {
        String name = get("name");
        String description = get("description");
        if (StringUtils.isEmpty(name)) {
            renderText("名称参数不可为空");
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode editorNode = objectMapper.createObjectNode();
        editorNode.put("id", "canvas");
        editorNode.put("resourceId", "canvas");
        ObjectNode stencilSetNode = objectMapper.createObjectNode();
        // 这个很重要，不能轻易改动 (改动后可能造成异常情况）
        stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
        editorNode.put("stencilset", stencilSetNode);

        ObjectNode modelObjectNode = objectMapper.createObjectNode();
        modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
        modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
        modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description == null ? "" : description);

        Model newModel = ActivitiUtils.getRepositoryService().newModel();
        newModel.setMetaInfo(modelObjectNode.toString());
        newModel.setName(name);
        ActivitiUtils.getRepositoryService().saveModel(newModel);
        ActivitiUtils.getRepositoryService().addModelEditorSource(newModel.getId(), editorNode.toString().getBytes(ActConst.DEFAULT_CHARSET));

        String editorUrl = "/static/plugins/activiti/modeler.html?modelId=" + newModel.getId();
        redirect(editorUrl);
    }


    // activiti modeler 打开时加载模型数据
    public void modelJson() {
        String modelId = get(0);
        JSONObject modelNode = new JSONObject();
        Model model = ActivitiUtils.getRepositoryService().getModel(modelId);
        if (model != null) {
            try {
                if (StringUtils.notEmpty(model.getMetaInfo())) {
                    modelNode = JSON.parseObject(model.getMetaInfo());
                } else {
                    modelNode.put("modelId", model.getName());
                }
                modelNode.put("modelId", model.getId());
                JSONObject editorJsonNode = JSON.parseObject(new String(ActivitiUtils.getRepositoryService().getModelEditorSource(model.getId()), ActConst.DEFAULT_CHARSET));
                modelNode.put("model", editorJsonNode);
            } catch (Exception e) {
                log.error("Error creating model JSON", e);
                throw new ActivitiException("Error creating model JSON", e);
            }
        }
        renderJson(modelNode);
    }

    // activti modeler 保存
    @Before(Tx.class)
    public void modelSave() throws TranscoderException, IOException {
        String modelId = get("modelId");
        String name = get("name");
        String description = get("description");
        String jsonXml = get("json_xml");
        String svgXml = get("svg_xml");
        if (StringUtils.isEmpty(modelId) || StringUtils.isEmpty(jsonXml) || StringUtils.isEmpty(svgXml) || StringUtils.isEmpty(name)) {
            renderFail("参数缺失");
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Model model = ActivitiUtils.getRepositoryService().getModel(modelId);
        ObjectNode modelJson = (ObjectNode) objectMapper.readTree(model.getMetaInfo());
        modelJson.put(ModelDataJsonConstants.MODEL_NAME, name);
        modelJson.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
        model.setMetaInfo(modelJson.toString());
        model.setName(name);

        ActivitiUtils.getRepositoryService().saveModel(model);
        ActivitiUtils.getRepositoryService().addModelEditorSource(model.getId(), jsonXml.getBytes(ActConst.DEFAULT_CHARSET));

        InputStream svgStream = new ByteArrayInputStream(svgXml.getBytes(ActConst.DEFAULT_CHARSET));
        TranscoderInput input = new TranscoderInput(svgStream);
        PNGTranscoder transcoder = new PNGTranscoder();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        TranscoderOutput output = new TranscoderOutput(outStream);
        transcoder.transcode(input, output);
        final byte[] result = outStream.toByteArray();
        ActivitiUtils.getRepositoryService().addModelEditorSourceExtra(model.getId(), result);
        outStream.close();

        Map<String, Object> ret = new HashMap<>();
        ret.put("lastUpdated", model.getLastUpdateTime());
        renderJson(ret);
    }

    // 模型部署
    @Before(IdRequired.class)
    public void modelDeploy() throws IOException {
        String modelId = get("id");
        Model model = ActivitiUtils.getRepositoryService().getModel(modelId);
        if (model == null) {
            renderFail("参数错误");
            return;
        }
        ObjectNode modelNode = (ObjectNode) new ObjectMapper()
                .readTree(ActivitiUtils.getRepositoryService().getModelEditorSource(model.getId()));
        BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(modelNode);

        // 如果流程定义中 【名称】 和 【备注】没有填写，使用 【模型名称】和【模型描述】
        // 这样是为了 流程定义转换为 模型是 ，模型能有 名称和 描述
        if (StringUtils.isEmpty(bpmnModel.getMainProcess().getDocumentation())) {
            bpmnModel.getMainProcess().setDocumentation((String) JSON.parseObject(model.getMetaInfo()).get("description"));
        }
        if (StringUtils.isEmpty(bpmnModel.getMainProcess().getName())) {
            bpmnModel.getMainProcess().setName(model.getName());
        }

        byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(bpmnModel);
        Deployment deployment = ActivitiUtils.getRepositoryService().createDeployment()
                .name("通过model部署，modelId: " + model.getId())
                .addString(bpmnModel.getMainProcess().getId() + ".bpmn", new String(bpmnBytes, ActConst.DEFAULT_CHARSET))
                .deploy();
        renderSuccess("部署成功，部署ID=" + deployment.getId());
    }
}
