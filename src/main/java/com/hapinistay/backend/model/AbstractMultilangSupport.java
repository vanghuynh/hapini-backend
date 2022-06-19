package com.hapinistay.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class AbstractMultilangSupport<T> {


  public AbstractMultilangSupport() {
  }
  
  
    @JsonIgnore
    public abstract void getLanguageDto(String lang);
	
    @JsonIgnore
    public abstract void setLanguageDto(String lang);
	
    public abstract void setLanguageDto(String lang, T oldEntity);
  
}