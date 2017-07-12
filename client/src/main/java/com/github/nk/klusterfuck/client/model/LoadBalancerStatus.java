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
import com.github.nk.klusterfuck.client.model.LoadBalancerIngress;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * LoadBalancerStatus
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-07-12T18:36:23.255+05:30")
public class LoadBalancerStatus {
  @SerializedName("ingress")
  private List<LoadBalancerIngress> ingress = new ArrayList<LoadBalancerIngress>();

  public LoadBalancerStatus ingress(List<LoadBalancerIngress> ingress) {
    this.ingress = ingress;
    return this;
  }

  public LoadBalancerStatus addIngressItem(LoadBalancerIngress ingressItem) {
    this.ingress.add(ingressItem);
    return this;
  }

   /**
   * Get ingress
   * @return ingress
  **/
  @ApiModelProperty(example = "null", value = "")
  public List<LoadBalancerIngress> getIngress() {
    return ingress;
  }

  public void setIngress(List<LoadBalancerIngress> ingress) {
    this.ingress = ingress;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LoadBalancerStatus loadBalancerStatus = (LoadBalancerStatus) o;
    return Objects.equals(this.ingress, loadBalancerStatus.ingress);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ingress);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LoadBalancerStatus {\n");
    
    sb.append("    ingress: ").append(toIndentedString(ingress)).append("\n");
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

