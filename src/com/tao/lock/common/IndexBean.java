package com.tao.lock.common;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tao.lock.qrservice.QRUtils;

@Named
@SessionScoped
public class IndexBean implements Serializable {
 
	private static final long serialVersionUID = 1L;
 
	private static final Logger LOGGER = LoggerFactory.getLogger(IndexBean.class);

	private String qrText;
	
	private String qrCodePath;
	
	public IndexBean() {
		
	}
	
	public String renderQR() {
		QRUtils qrUtils = new QRUtils();
		qrCodePath = qrUtils.renderQR(qrText);	
		return "true";
	}


	public String getQrText() {
		return qrText;
	}

	public void setQrText(String qrText) {
		this.qrText = qrText;
	}

	public String getQrCodePath() {
		return qrCodePath;
	}

	public void setQrCodePath(String qrCodePath) {
		this.qrCodePath = qrCodePath;
	}
}
