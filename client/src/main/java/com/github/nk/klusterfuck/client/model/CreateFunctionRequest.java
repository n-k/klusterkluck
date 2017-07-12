/*
 * API
 * API
 *
 * OpenAPI spec version: v1
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.github.nk.klusterfuck.client.model;

import java.util.Objects;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * CreateFunctionRequest
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-07-12T18:36:23.255+05:30")
public class CreateFunctionRequest {
  @SerializedName("name")
  private String name = null;

  @SerializedName("template")
  private String template = null;

  /**
   * Gets or Sets serviceType
   */
  public enum ServiceTypeEnum {
    @SerializedName("ClusterIP")
    CLUSTERIP("ClusterIP"),
    
    @SerializedName("NodePort")
    NODEPORT("NodePort");

    private String value;

    ServiceTypeEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  @SerializedName("serviceType")
  private ServiceTypeEnum serviceType = null;

  @SerializedName("ingress")
  private Boolean ingress = false;

  @SerializedName("host")
  private String host = null;

  @SerializedName("path")
  private String path = null;

  public CreateFunctionRequest name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Get name
   * @return name
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public CreateFunctionRequest template(String template) {
    this.template = template;
    return this;
  }

   /**
   * Get template
   * @return template
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getTemplate() {
    return template;
  }

  public void setTemplate(String template) {
    this.template = template;
  }

  public CreateFunctionRequest serviceType(ServiceTypeEnum serviceType) {
    this.serviceType = serviceType;
    return this;
  }

   /**
   * Get serviceType
   * @return serviceType
  **/
  @ApiModelProperty(example = "null", value = "")
  public ServiceTypeEnum getServiceType() {
    return serviceType;
  }

  public void setServiceType(ServiceTypeEnum serviceType) {
    this.serviceType = serviceType;
  }

  public CreateFunctionRequest ingress(Boolean ingress) {
    this.ingress = ingress;
    return this;
  }

   /**
   * Get ingress
   * @return ingress
  **/
  @ApiModelProperty(example = "null", value = "")
  public Boolean getIngress() {
    return ingress;
  }

  public void setIngress(Boolean ingress) {
    this.ingress = ingress;
  }

  public CreateFunctionRequest host(String host) {
    this.host = host;
    return this;
  }

   /**
   * Get host
   * @return host
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public CreateFunctionRequest path(String path) {
    this.path = path;
    return this;
  }

   /**
   * Get path
   * @return path
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateFunctionRequest createFunctionRequest = (CreateFunctionRequest) o;
    return Objects.equals(this.name, createFunctionRequest.name) &&
        Objects.equals(this.template, createFunctionRequest.template) &&
        Objects.equals(this.serviceType, createFunctionRequest.serviceType) &&
        Objects.equals(this.ingress, createFunctionRequest.ingress) &&
        Objects.equals(this.host, createFunctionRequest.host) &&
        Objects.equals(this.path, createFunctionRequest.path);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, template, serviceType, ingress, host, path);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateFunctionRequest {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    template: ").append(toIndentedString(template)).append("\n");
    sb.append("    serviceType: ").append(toIndentedString(serviceType)).append("\n");
    sb.append("    ingress: ").append(toIndentedString(ingress)).append("\n");
    sb.append("    host: ").append(toIndentedString(host)).append("\n");
    sb.append("    path: ").append(toIndentedString(path)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
  
}

