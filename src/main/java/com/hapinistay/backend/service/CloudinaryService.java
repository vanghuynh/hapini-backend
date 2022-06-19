package com.hapinistay.backend.service;

import java.io.IOException;
import java.util.List;

import com.cloudinary.Cloudinary;
import com.hapinistay.backend.model.Image;

public interface CloudinaryService {
	Cloudinary getCloudinaryInstance();
	Image uploadFileToCloudinary(byte[] bytes) throws IOException;
	boolean deleteImage(String public_id, Integer image_id) throws Exception;
	boolean deleteImage(List<String> publicIds) throws Exception;
}
