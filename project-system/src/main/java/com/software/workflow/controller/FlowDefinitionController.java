package com.software.workflow.controller;

import cn.hutool.http.server.HttpServerResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.annotation.OperationLog;
import com.software.exception.BadRequestException;
import com.software.workflow.dto.FlowProcDefDto;
import com.software.workflow.dto.FlowSaveXmlDto;
import com.software.workflow.service.FlowDefinitionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

/**
 * @author suaxi
 * @date 2024/12/12 16:11
 */
@Slf4j
@RestController
@RequestMapping("/workflow/definition")
@Api(tags = "流程定义接口")
public class FlowDefinitionController {

    @Autowired
    private FlowDefinitionService flowDefinitionService;

    @ApiOperation(value = "流程定义列表（分页）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "流程名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "页数", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, paramType = "query", dataType = "String")
    })
    @GetMapping(value = "/list")
    public ResponseEntity<Page<FlowProcDefDto>> list(@RequestParam(value = "name", required = false) String name,
                                                     @RequestParam("pageNum") Integer pageNum,
                                                     @RequestParam("pageSize") Integer pageSize) {
        return new ResponseEntity<>(flowDefinitionService.list(name, pageNum, pageSize), HttpStatus.OK);
    }

    @ApiOperation(value = "导入流程文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "流程文件名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "category", value = "流程文件分类", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "file", value = "流程文件（bpmn2.0 xml）", required = true, paramType = "formData", dataType = "file")
    })
    @PostMapping("/import")
    public ResponseEntity<?> importFile(@RequestParam(value = "name", required = false) String name,
                                        @RequestParam(value = "category", required = false) String category,
                                        @RequestParam("file") MultipartFile file) {
        InputStream in = null;
        try {
            in = file.getInputStream();
            flowDefinitionService.importFile(name, category, in);
        } catch (Exception e) {
            log.error("导入流程文件一场：{}", e.getMessage());
            throw new BadRequestException("导入流程文件失败，请联系管理员！");
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>("导入成功", HttpStatus.OK);
    }

    @ApiOperation(value = "读取xml")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deployId", value = "流程部署ID", required = true, paramType = "query", dataType = "String")
    })
    @GetMapping(value = "/readXml")
    public ResponseEntity<String> readXml(@RequestParam("deployId") String deployId) throws IOException {
        return new ResponseEntity<>(flowDefinitionService.readXml(deployId), HttpStatus.OK);
    }

    @ApiOperation(value = "读取图片文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deployId", value = "流程部署ID", required = true, paramType = "query", dataType = "String")
    })
    @GetMapping(value = "/readImage")
    public void readImage(@RequestParam("deployId") String deployId, HttpServerResponse resp) {
        OutputStream os = null;
        BufferedImage image;
        try {
            image = ImageIO.read(flowDefinitionService.readImage(deployId));
            resp.setContentType("image/png");
            os = resp.getOut();
            if (image != null) {
                ImageIO.write(image, "png", os);
            }
        } catch (Exception e) {
            log.error("读取流程部署实例图片异常：{}", e.getMessage());
            throw new BadRequestException("读取流程部署实例图片失败，请联系管理员！");
        } finally {
            try {
                if (os != null) {
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @ApiOperation(value = "保存流程设计器中的xml文件")
    @PostMapping("/save")
    @OperationLog("保存流程设计器中的xml文件")
    public ResponseEntity<?> save(@RequestBody FlowSaveXmlDto flowSaveXmlDto) {
        InputStream in = null;
        try {
            in = new ByteArrayInputStream(flowSaveXmlDto.getXml().getBytes(StandardCharsets.UTF_8));
            flowDefinitionService.importFile(flowSaveXmlDto.getName(), flowSaveXmlDto.getCategory(), in);
        } catch (Exception e) {
            log.error("保存流程设计器中的xml文件异常：{}", e.getMessage());
            throw new BadRequestException("保存失败，请联系管理员！");
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>("保存成功！", HttpStatus.OK);
    }

    @ApiOperation(value = "发起流程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "procDefId", value = "流程定义ID", required = true, paramType = "path", dataType = "String"),
            @ApiImplicitParam(name = "variables", value = "流程变量", required = true, paramType = "body", dataType = "object")
    })
    @PostMapping("/startProcess/{procDefId}")
    @OperationLog("发起流程")
    public ResponseEntity<Boolean> startProcess(@PathVariable(value = "procDefId") String procDefId, @RequestBody Map<String, Object> variables) {
        return new ResponseEntity<>(flowDefinitionService.startProcessInstanceById(procDefId, variables), HttpStatus.OK);
    }

    @ApiOperation(value = "激活或挂起流程定义")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "state", value = "状态（1：激活，2：挂起）", required = true, paramType = "path", dataType = "Integer"),
            @ApiImplicitParam(name = "deployId", value = "流程部署ID", required = true, paramType = "path", dataType = "String")
    })
    @PutMapping("/updateState/{state}/{deployId}")
    @OperationLog("激活或挂起流程定义")
    public ResponseEntity<?> startProcess(@PathVariable(value = "state") Integer state, @PathVariable(value = "deployId") String deployId) {
        if (!Arrays.asList(1, 2).contains(state)) {
            throw new BadRequestException("状态不正确！");
        }
        flowDefinitionService.updateState(state, deployId);
        return new ResponseEntity<>("操作成功！", HttpStatus.OK);
    }

    @ApiOperation(value = "删除流程定义")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deployId", value = "流程部署ID", required = true, paramType = "path", dataType = "String")
    })
    @DeleteMapping("/startProcess/{deployId}")
    @OperationLog("删除流程定义")
    public ResponseEntity<?> startProcess(@PathVariable(value = "deployId") String deployId) {
        flowDefinitionService.delete(deployId);
        return new ResponseEntity<>("删除成功！", HttpStatus.OK);
    }
}
