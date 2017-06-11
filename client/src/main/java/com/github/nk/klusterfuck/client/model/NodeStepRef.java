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
import com.github.nk.klusterfuck.client.model.StepRef;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * NodeStepRef
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-06-11T23:32:22.014+05:30")
public class NodeStepRef {
  @SerializedName("id")
  private String id = null;

  @SerializedName("data")
  private StepRef data = null;

  @SerializedName("uiProps")
  private Map<String, Object> uiProps = new HashMap<String, Object>();

  public NodeStepRef id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public NodeStepRef data(StepRef data) {
    this.data = data;
    return this;
  }

   /**
   * Get data
   * @return data
  **/
  @ApiModelProperty(example = "null", value = "")
  public StepRef getData() {
    return data;
  }

  public void setData(StepRef data) {
    this.data = data;
  }

  public NodeStepRef uiProps(Map<String, Object> uiProps) {
    this.uiProps = uiProps;
    return this;
  }

  public NodeStepRef putUiPropsItem(String key, Object uiPropsItem) {
    this.uiProps.put(key, uiPropsItem);
    return this;
  }

   /**
   * Get uiProps
   * @return uiProps
  **/
  @ApiModelProperty(example = "null", value = "")
  public Map<String, Object> getUiProps() {
    return uiProps;
  }

  public void setUiProps(Map<String, Object> uiProps) {
    this.uiProps = uiProps;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NodeStepRef nodeStepRef = (NodeStepRef) o;
    return Objects.equals(this.id, nodeStepRef.id) &&
        Objects.equals(this.data, nodeStepRef.data) &&
        Objects.equals(this.uiProps, nodeStepRef.uiProps);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, data, uiProps);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NodeStepRef {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    uiProps: ").append(toIndentedString(uiProps)).append("\n");
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
