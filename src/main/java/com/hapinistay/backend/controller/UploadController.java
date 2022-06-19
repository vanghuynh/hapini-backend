package com.hapinistay.backend.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hapinistay.backend.dto.ResponseDto;
import com.hapinistay.backend.model.Apartment;
import com.hapinistay.backend.model.House;
import com.hapinistay.backend.model.Image;
import com.hapinistay.backend.model.Room;
import com.hapinistay.backend.service.CloudinaryService;
import com.hapinistay.backend.service.ImageService;

@RestController
@RequestMapping("/upload")
public class UploadController {

	public static final Logger logger = LoggerFactory.getLogger(UploadController.class);

	@Autowired
	private CloudinaryService cloudinaryService;
	
	@Autowired
	private ImageService imageService;
	
	//Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "/Users/huynhvang/Downloads/temp/";
    
	/**
	 * Upload file to server
	 * @param uploadfile
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto> uploadFile(@RequestParam("file") MultipartFile uploadfile) {
		if (uploadfile.isEmpty()) {
			return new ResponseEntity<>(new ResponseDto("File is empty", null), HttpStatus.NOT_FOUND);
		}
		//upload file to server
		try {
            saveUploadedFiles(Arrays.asList(uploadfile));
        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseDto("Error while uploading file", null),HttpStatus.NOT_FOUND);
        }
		return new ResponseEntity<>(new ResponseDto("OK", uploadfile.getOriginalFilename()), HttpStatus.OK);
	}
	
    /**
     * Upload many files to local PC, just for testing
     * @param files
     * @throws IOException
     */
    private void saveUploadedFiles(List<MultipartFile> files) throws IOException {
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);
        }
    }
    
    /**
     * Upload file to cloud server
     * @param uploadfile
     * @return
     */
    @RequestMapping(value = "/cloudinary", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto> uploadFileToCloudinary(@RequestParam("file") MultipartFile uploadfile) {
		if (uploadfile.isEmpty()) {
			return new ResponseEntity<>(new ResponseDto("Upload file is empty", null), HttpStatus.NOT_FOUND);
		}
		//upload file to server
		Image result = null;
		try {
            result = this.cloudinaryService.uploadFileToCloudinary(uploadfile.getBytes());
        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseDto("Error while uploading file", null), HttpStatus.NOT_FOUND);
        }
		return new ResponseEntity<>(new ResponseDto("OK", result), HttpStatus.OK);
	}
    
 	/**
 	 * Get all images in database
 	 * @return
 	 */
 	@RequestMapping(value = "/images", method = RequestMethod.GET)
 	public ResponseEntity<ResponseDto> listAllImagesInDatabase() {
 		List<Image> images = imageService.findAllImages();
 		if (images.isEmpty()) {
 			return new ResponseEntity<>(new ResponseDto("NO_CONTENT", null), HttpStatus.NO_CONTENT);
 		}
 		return new ResponseEntity<>(new ResponseDto("OK", images), HttpStatus.OK);
 	}
 	
 	/**
 	 * Delete image on server and database
 	 * @param public_id
 	 * @param image_id
 	 * @return
 	 */
 	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto> deleteFileCloudinary(@RequestParam("public_id") String public_id, @RequestParam("image_id") Integer image_id) {
		if (public_id.isEmpty() || image_id == null) {
			return new ResponseEntity<>(new ResponseDto("NOT_FOUND", null), HttpStatus.NOT_FOUND);
		}
		boolean result;
		try {
            result = this.cloudinaryService.deleteImage(public_id, image_id);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto("Error while deleting image", null), HttpStatus.NOT_FOUND);
        }
		
		return new ResponseEntity<>(new ResponseDto("OK", result), HttpStatus.OK);
	}
 	
 	
 	/**
 	 * Upload image for house
 	 * @param uploadfile
 	 * @param houseId
 	 * @return
 	 */
 	@RequestMapping(value = "/house", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto> uploadHouseImageCloudinary(@RequestParam("file") MultipartFile uploadfile, @RequestParam("houseId") Long houseId) {
		if (uploadfile.isEmpty()) {
			return new ResponseEntity<>(new ResponseDto("Upload image is empty", null), HttpStatus.NOT_FOUND);
		}
		//upload file to server
		House house;
		Image result;
		try {
            result = this.cloudinaryService.uploadFileToCloudinary(uploadfile.getBytes());
            house = this.imageService.saveImageOfHouse(result, houseId);
            result.setHouse(house);
            this.imageService.saveImage(result);
        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseDto("Error while uploading image", null), HttpStatus.NOT_FOUND);
        }
		return new ResponseEntity<>(new ResponseDto("OK", result), HttpStatus.OK);
	}
 	
 	/**
 	 * Upload house avatar
 	 * @param uploadfile
 	 * @param houseId
 	 * @return
 	 */
 	@RequestMapping(value = "/house/avatar", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto> uploadHouseAvatarCloudinary(@RequestParam("file") MultipartFile uploadfile, @RequestParam("houseId") Long houseId) {
		if (uploadfile.isEmpty()) {
			return new ResponseEntity<>(new ResponseDto("Upload image is empty", null), HttpStatus.NOT_FOUND);
		}
		//upload file to server
		House house;
		Image result;
		try {
            result = this.cloudinaryService.uploadFileToCloudinary(uploadfile.getBytes());
            house = this.imageService.saveAvatarOfHouse(result, houseId);
        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseDto("Error while uploading image", null), HttpStatus.NOT_FOUND);
        }
		return new ResponseEntity<>(new ResponseDto("OK", house), HttpStatus.OK);
	}
 	
 	/**
 	 * Upload room avatar
 	 * @param uploadfile
 	 * @param roomId
 	 * @return
 	 */
 	@RequestMapping(value = "/room/avatar", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto> uploadRoomAvatarCloudinary(@RequestParam("file") MultipartFile uploadfile, 
			@RequestParam("roomId") Long roomId) {
		if (uploadfile.isEmpty()) {
			return new ResponseEntity<>(new ResponseDto("Upload image is empty", null), HttpStatus.NOT_FOUND);
		}
		//upload file to server
		Room room;
		try {
            Image result = this.cloudinaryService.uploadFileToCloudinary(uploadfile.getBytes());
            room = this.imageService.saveAvatarOfRoom(result, roomId);
        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseDto("Error while uploading image", null), HttpStatus.NOT_FOUND);
        }
		return new ResponseEntity<>(new ResponseDto("OK", room), HttpStatus.OK);
	}
 	
 	/**
 	 * Upload room image
 	 * @param uploadfile
 	 * @param roomId
 	 * @return
 	 */
 	@RequestMapping(value = "/room", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto> uploadRoomImage(@RequestParam("file") MultipartFile uploadfile, 
			@RequestParam("roomId") Long roomId) {
		if (uploadfile.isEmpty()) {
			return new ResponseEntity<>(new ResponseDto("Upload image is empty", null), HttpStatus.NOT_FOUND);
		}
		//upload file to server
		Room room;
		Image result;
		try {
            result = this.cloudinaryService.uploadFileToCloudinary(uploadfile.getBytes());
            room = this.imageService.saveImageOfRoom(result, roomId);
            result.setRoom(room);
            this.imageService.saveImage(result);
        } catch (IOException e) {
        	return new ResponseEntity<>(new ResponseDto("Error while uploading image", null), HttpStatus.NOT_FOUND);
        }
		
		return new ResponseEntity<>(new ResponseDto("OK", result), HttpStatus.OK);
	}
 	
 	@RequestMapping(value = "/apartment", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto> uploadApartmentImage(@RequestParam("file") MultipartFile uploadfile, 
			@RequestParam("apartmentId") Long apartmentId) {
		if (uploadfile.isEmpty()) {
			return new ResponseEntity<>(new ResponseDto("Upload image is empty", null), HttpStatus.NOT_FOUND);
		}
		//upload file to server
		Apartment apartment;
		Image result;
		try {
            result = this.cloudinaryService.uploadFileToCloudinary(uploadfile.getBytes());
            apartment = this.imageService.saveImageOfApartment(result, apartmentId);
            result.setApartment(apartment);
            this.imageService.saveImage(result);
        } catch (IOException e) {
        	return new ResponseEntity<>(new ResponseDto("Error while uploading image", null), HttpStatus.NOT_FOUND);
        }
		
		return new ResponseEntity<>(new ResponseDto("OK", result), HttpStatus.OK);
	}
}