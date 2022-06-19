package com.hapinistay.backend.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;
import com.hapinistay.backend.model.Image;
import com.hapinistay.backend.repositories.ImageRepository;

@Service
public class CloudinaryServiceImpl implements CloudinaryService{

	private Cloudinary cloudinary;
	
	@Autowired
	private ImageRepository imageRepository;
	
	@Autowired
	private Environment environment;
	
	@Override
	public Cloudinary getCloudinaryInstance() {
		if(cloudinary == null){
			Map config = ObjectUtils.asMap(
					  "cloud_name", environment.getRequiredProperty("CLOUDINARY_CLOUD_NAME"),
					  "api_key", environment.getRequiredProperty("CLOUDINARY_API_KEY"),
					  "api_secret", environment.getRequiredProperty("CLOUDINARY_API_SECRET"));
			cloudinary = new Cloudinary(config);
		}
		return cloudinary;
	}

	@Override
	public Image uploadFileToCloudinary(byte[] bytes) throws IOException {
		Map uploadResult = this.getCloudinaryInstance().uploader().upload(bytes, ObjectUtils.asMap("transformation", 
				new Transformation()
				.width(1000).height(1000).crop("limit")
				//.height(400).crop("scale")
				));
        String url = (String)uploadResult.get("url");
        String secure_url = (String)uploadResult.get("secure_url");
        String public_id = (String)uploadResult.get("public_id");
        
        Image image= new Image();
        image.setUrl(url);
        image.setSecure_url(secure_url);
        image.setPublic_id(public_id);
        
        imageRepository.save(image);
        
		return image;
	}

	@Override
	public boolean deleteImage(String public_id, Integer image_id) throws Exception {
		ApiResponse result = this.getCloudinaryInstance().api().deleteResources(Arrays.asList(public_id),
			    ObjectUtils.emptyMap());
		if(result.containsKey("deleted")) {
			System.out.println(result);
			//imageRepository.delete(image_id.longValue());
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteImage(List<String> publicIds) throws Exception {
		ApiResponse result = this.getCloudinaryInstance().api().deleteResources(publicIds,
			    ObjectUtils.emptyMap());
		System.out.println("delete image " + result);
		if(result.containsKey("deleted")) {
			System.out.println(result);
			return true;
		}
		return false;
	}

}
