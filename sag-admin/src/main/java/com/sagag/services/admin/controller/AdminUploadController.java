package com.sagag.services.admin.controller;

import com.sagag.services.admin.swagger.docs.ApiDesc.AdminFileUpload;
import com.sagag.services.common.external.FileUploadDto;
import com.sagag.services.common.external.FileUploadType;
import com.sagag.services.common.external.IUploadProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/admin/upload")
@Api(tags = "admin upload APIs")
public class AdminUploadController {

  @Autowired
  private List<IUploadProcessor> processors;

  @ApiOperation(value = AdminFileUpload.UPLOAD_FILE_DESC, notes = AdminFileUpload.UPLOAD_FILE_NOTE)
  @PostMapping(value = "/media/{uploadType}", produces = MediaType.APPLICATION_JSON_VALUE)
  public FileUploadDto uploadToMediaServer(@RequestParam("file") MultipartFile file,
      @PathVariable("uploadType") FileUploadType type) throws IOException {
    IUploadProcessor processor = processors.stream().filter(p -> p.isSupport(type)).findAny()
        .orElseThrow(() -> new IllegalArgumentException("Upload Type not supported"));
    return processor.upload(file).orElseThrow(() -> new IllegalArgumentException("Failed to upload"));
  }

  @ApiOperation(value = AdminFileUpload.REMOVE_FILE_DESC, notes = AdminFileUpload.REMOVE_FILE_NOTE)
  @DeleteMapping(value = "/remove/{uploadType}/{fileName}", produces = MediaType.APPLICATION_JSON_VALUE)
  public void removeFromMediaServer(@PathVariable("fileName") String fileName,
      @PathVariable("uploadType") FileUploadType type) throws IOException {
    IUploadProcessor processor = processors.stream().filter(p -> p.isSupport(type)).findAny()
        .orElseThrow(() -> new IllegalArgumentException("Upload Type not supported"));
    processor.remove(fileName);
  }

}
