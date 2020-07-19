package com.github.qinyou.process.controller;

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
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("Duplicates")
@Slf4j
@RequirePermission("processDefine")
public class ProcessDefineController extends BaseController {

    public void index() {
        render("process/processDefine.ftl");
    }

    // 流程定义数据
    public void query() {
        int pageNumber = getInt("page", 1);
        int pageSize = getInt("rows", 30);

        String name = get("extra_name");
        String key = getPara("extra_key");
        RepositoryService service = ActivitiUtils.getRepositoryService();
        ProcessDefinitionQuery query = service.createProcessDefinitionQuery();
        if (StringUtils.notEmpty(name)) {
            query.processDefinitionNameLike("%" + name + "%");
        }
        if (StringUtils.notEmpty(key)) {
            query.processDefinitionKeyLike("%" + key + "%");
        }

        List<Map<String, Object>> list = new ArrayList<>();
        query.orderByProcessDefinitionCategory().asc()
                .orderByProcessDefinitionKey().desc()
                .orderByProcessDefinitionVersion().desc()
                .listPage((pageNumber - 1) * pageSize, pageSize)
                .forEach(definition -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", definition.getId());
                    item.put("key", definition.getKey());
                    item.put("version", definition.getVersion());
                    if (definition.getName() != null) {
                        item.put("name", definition.getName());
                    }
                    if (definition.getDescription() != null) {
                        item.put("description", definition.getDescription());
                    }
                    item.put("deploymentId", definition.getDeploymentId());
                    item.put("state", !service.isProcessDefinitionSuspended(definition.getId()));
                    list.add(item);
                });

        Long total = query.count();
        renderDatagrid(list, total.intValue());
    }


    // 激活操作
    @Before({IdsRequired.class, Tx.class})
    public void activateProcessDefine() {
        String ids = getPara("ids");
        RepositoryService service = ActivitiUtils.getRepositoryService();
        for (String id : ids.split(",")) {
            if (service.isProcessDefinitionSuspended(id)) {
                // 激活定义的同时激活流程实例
                service.activateProcessDefinitionById(id, true, null);
            }
        }
        renderSuccess("激活成功");
    }

    // 挂起操作
    @Before({IdsRequired.class, Tx.class})
    public void suspendProcessDefine() {
        String ids = getPara("ids");
        RepositoryService service = ActivitiUtils.getRepositoryService();
        for (String id : ids.split(",")) {
            if (!service.isProcessDefinitionSuspended(id)) {
                // 挂起定义的同时 挂起流程实例
                service.suspendProcessDefinitionById(id, true, null);
            }
        }
        renderSuccess("挂起成功");
    }

    // 流程定义转模型
    @Before({IdRequired.class, Tx.class})
    public void convertToModel() throws UnsupportedEncodingException, XMLStreamException {
        String processDefinitionId = get("id"); // 流程定义id
        ProcessDefinition processDefinition = ActivitiUtils.getRepositoryService()
                .createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();
        if (processDefinition == null) {
            renderFail("流程定义id参数错误");
            return;
        }

        InputStream bpmnStream = ActivitiUtils.getRepositoryService()
                .getResourceAsStream(processDefinition.getDeploymentId(),
                        processDefinition.getResourceName());
        XMLInputFactory xif = XMLInputFactory.newInstance();
        InputStreamReader in = new InputStreamReader(bpmnStream, ActConst.DEFAULT_CHARSET);
        XMLStreamReader xtr = xif.createXMLStreamReader(in);
        BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);
        BpmnJsonConverter converter = new BpmnJsonConverter();

        ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
        modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, processDefinition.getName());
        modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, processDefinition.getVersion());
        modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, processDefinition.getDescription());

        Model model = ActivitiUtils.getRepositoryService().newModel();
        model.setMetaInfo(modelObjectNode.toString());
        model.setName(processDefinition.getName());

        ActivitiUtils.getRepositoryService().saveModel(model);
        ObjectNode modelNode = converter.convertToJson(bpmnModel);
        ActivitiUtils.getRepositoryService().addModelEditorSource(model.getId(), modelNode.toString().getBytes(ActConst.DEFAULT_CHARSET));

        renderSuccess("转化成功：modelId: " + model.getId());
    }
}
