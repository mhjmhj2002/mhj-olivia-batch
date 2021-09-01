package com.mhj.olivia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FinalizarFileService {
	
	@Value("files.path.success")
	private String successPath;
	
	//TODO:zipservice
	
	

}
