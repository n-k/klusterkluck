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
import com.github.nk.klusterfuck.client.model.ServicePort;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ServiceSpec
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-07-12T18:51:26.870+05:30")
public class ServiceSpec {
  @SerializedName("clusterIP")
  private String clusterIP = null;

  @SerializedName("deprecatedPublicIPs")
  private List<String> deprecatedPublicIPs = new ArrayList<String>();

  @SerializedName("externalIPs")
  private List<String> externalIPs = new ArrayList<String>();

  @SerializedName("externalName")
  private String externalName = null;

  @SerializedName("loadBalancerIP")
  private String loadBalancerIP = null;

  @SerializedName("loadBalancerSourceRanges")
  private List<String> loadBalancerSourceRanges = new ArrayList<String>();

  @SerializedName("ports")
  private List<ServicePort> ports = new ArrayList<ServicePort>();

  @SerializedName("selector")
  private Map<String, String> selector = new HashMap<String, String>();

  @SerializedName("sessionAffinity")
  private String sessionAffinity = null;

  @SerializedName("type")
  private String type = null;

  public ServiceSpec clusterIP(String clusterIP) {
    this.clusterIP = clusterIP;
    return this;
  }

   /**
   * Get clusterIP
   * @return clusterIP
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getClusterIP() {
    return clusterIP;
  }

  public void setClusterIP(String clusterIP) {
    this.clusterIP = clusterIP;
  }

  public ServiceSpec deprecatedPublicIPs(List<String> deprecatedPublicIPs) {
    this.deprecatedPublicIPs = deprecatedPublicIPs;
    return this;
  }

  public ServiceSpec addDeprecatedPublicIPsItem(String deprecatedPublicIPsItem) {
    this.deprecatedPublicIPs.add(deprecatedPublicIPsItem);
    return this;
  }

   /**
   * Get deprecatedPublicIPs
   * @return deprecatedPublicIPs
  **/
  @ApiModelProperty(example = "null", value = "")
  public List<String> getDeprecatedPublicIPs() {
    return deprecatedPublicIPs;
  }

  public void setDeprecatedPublicIPs(List<String> deprecatedPublicIPs) {
    this.deprecatedPublicIPs = deprecatedPublicIPs;
  }

  public ServiceSpec externalIPs(List<String> externalIPs) {
    this.externalIPs = externalIPs;
    return this;
  }

  public ServiceSpec addExternalIPsItem(String externalIPsItem) {
    this.externalIPs.add(externalIPsItem);
    return this;
  }

   /**
   * Get externalIPs
   * @return externalIPs
  **/
  @ApiModelProperty(example = "null", value = "")
  public List<String> getExternalIPs() {
    return externalIPs;
  }

  public void setExternalIPs(List<String> externalIPs) {
    this.externalIPs = externalIPs;
  }

  public ServiceSpec externalName(String externalName) {
    this.externalName = externalName;
    return this;
  }

   /**
   * Get externalName
   * @return externalName
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getExternalName() {
    return externalName;
  }

  public void setExternalName(String externalName) {
    this.externalName = externalName;
  }

  public ServiceSpec loadBalancerIP(String loadBalancerIP) {
    this.loadBalancerIP = loadBalancerIP;
    return this;
  }

   /**
   * Get loadBalancerIP
   * @return loadBalancerIP
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getLoadBalancerIP() {
    return loadBalancerIP;
  }

  public void setLoadBalancerIP(String loadBalancerIP) {
    this.loadBalancerIP = loadBalancerIP;
  }

  public ServiceSpec loadBalancerSourceRanges(List<String> loadBalancerSourceRanges) {
    this.loadBalancerSourceRanges = loadBalancerSourceRanges;
    return this;
  }

  public ServiceSpec addLoadBalancerSourceRangesItem(String loadBalancerSourceRangesItem) {
    this.loadBalancerSourceRanges.add(loadBalancerSourceRangesItem);
    return this;
  }

   /**
   * Get loadBalancerSourceRanges
   * @return loadBalancerSourceRanges
  **/
  @ApiModelProperty(example = "null", value = "")
  public List<String> getLoadBalancerSourceRanges() {
    return loadBalancerSourceRanges;
  }

  public void setLoadBalancerSourceRanges(List<String> loadBalancerSourceRanges) {
    this.loadBalancerSourceRanges = loadBalancerSourceRanges;
  }

  public ServiceSpec ports(List<ServicePort> ports) {
    this.ports = ports;
    return this;
  }

  public ServiceSpec addPortsItem(ServicePort portsItem) {
    this.ports.add(portsItem);
    return this;
  }

   /**
   * Get ports
   * @return ports
  **/
  @ApiModelProperty(example = "null", value = "")
  public List<ServicePort> getPorts() {
    return ports;
  }

  public void setPorts(List<ServicePort> ports) {
    this.ports = ports;
  }

  public ServiceSpec selector(Map<String, String> selector) {
    this.selector = selector;
    return this;
  }

  public ServiceSpec putSelectorItem(String key, String selectorItem) {
    this.selector.put(key, selectorItem);
    return this;
  }

   /**
   * Get selector
   * @return selector
  **/
  @ApiModelProperty(example = "null", value = "")
  public Map<String, String> getSelector() {
    return selector;
  }

  public void setSelector(Map<String, String> selector) {
    this.selector = selector;
  }

  public ServiceSpec sessionAffinity(String sessionAffinity) {
    this.sessionAffinity = sessionAffinity;
    return this;
  }

   /**
   * Get sessionAffinity
   * @return sessionAffinity
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getSessionAffinity() {
    return sessionAffinity;
  }

  public void setSessionAffinity(String sessionAffinity) {
    this.sessionAffinity = sessionAffinity;
  }

  public ServiceSpec type(String type) {
    this.type = type;
    return this;
  }

   /**
   * Get type
   * @return type
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServiceSpec serviceSpec = (ServiceSpec) o;
    return Objects.equals(this.clusterIP, serviceSpec.clusterIP) &&
        Objects.equals(this.deprecatedPublicIPs, serviceSpec.deprecatedPublicIPs) &&
        Objects.equals(this.externalIPs, serviceSpec.externalIPs) &&
        Objects.equals(this.externalName, serviceSpec.externalName) &&
        Objects.equals(this.loadBalancerIP, serviceSpec.loadBalancerIP) &&
        Objects.equals(this.loadBalancerSourceRanges, serviceSpec.loadBalancerSourceRanges) &&
        Objects.equals(this.ports, serviceSpec.ports) &&
        Objects.equals(this.selector, serviceSpec.selector) &&
        Objects.equals(this.sessionAffinity, serviceSpec.sessionAffinity) &&
        Objects.equals(this.type, serviceSpec.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clusterIP, deprecatedPublicIPs, externalIPs, externalName, loadBalancerIP, loadBalancerSourceRanges, ports, selector, sessionAffinity, type);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServiceSpec {\n");
    
    sb.append("    clusterIP: ").append(toIndentedString(clusterIP)).append("\n");
    sb.append("    deprecatedPublicIPs: ").append(toIndentedString(deprecatedPublicIPs)).append("\n");
    sb.append("    externalIPs: ").append(toIndentedString(externalIPs)).append("\n");
    sb.append("    externalName: ").append(toIndentedString(externalName)).append("\n");
    sb.append("    loadBalancerIP: ").append(toIndentedString(loadBalancerIP)).append("\n");
    sb.append("    loadBalancerSourceRanges: ").append(toIndentedString(loadBalancerSourceRanges)).append("\n");
    sb.append("    ports: ").append(toIndentedString(ports)).append("\n");
    sb.append("    selector: ").append(toIndentedString(selector)).append("\n");
    sb.append("    sessionAffinity: ").append(toIndentedString(sessionAffinity)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
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

