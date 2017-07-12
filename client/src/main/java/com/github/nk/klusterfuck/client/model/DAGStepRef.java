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
import com.github.nk.klusterfuck.client.model.Link;
import com.github.nk.klusterfuck.client.model.NodeStepRef;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * DAGStepRef
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-07-12T17:03:22.568+05:30")
public class DAGStepRef {
  @SerializedName("nodes")
  private List<NodeStepRef> nodes = new ArrayList<NodeStepRef>();

  @SerializedName("links")
  private List<Link> links = new ArrayList<Link>();

  public DAGStepRef nodes(List<NodeStepRef> nodes) {
    this.nodes = nodes;
    return this;
  }

  public DAGStepRef addNodesItem(NodeStepRef nodesItem) {
    this.nodes.add(nodesItem);
    return this;
  }

   /**
   * Get nodes
   * @return nodes
  **/
  @ApiModelProperty(example = "null", value = "")
  public List<NodeStepRef> getNodes() {
    return nodes;
  }

  public void setNodes(List<NodeStepRef> nodes) {
    this.nodes = nodes;
  }

  public DAGStepRef links(List<Link> links) {
    this.links = links;
    return this;
  }

  public DAGStepRef addLinksItem(Link linksItem) {
    this.links.add(linksItem);
    return this;
  }

   /**
   * Get links
   * @return links
  **/
  @ApiModelProperty(example = "null", value = "")
  public List<Link> getLinks() {
    return links;
  }

  public void setLinks(List<Link> links) {
    this.links = links;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DAGStepRef daGStepRef = (DAGStepRef) o;
    return Objects.equals(this.nodes, daGStepRef.nodes) &&
        Objects.equals(this.links, daGStepRef.links);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nodes, links);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DAGStepRef {\n");
    
    sb.append("    nodes: ").append(toIndentedString(nodes)).append("\n");
    sb.append("    links: ").append(toIndentedString(links)).append("\n");
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
