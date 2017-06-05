/**
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

import * as models from './models';

export interface NodeStatus {
    addresses?: Array<models.NodeAddress>;

    allocatable?: { [key: string]: models.Quantity; };

    capacity?: { [key: string]: models.Quantity; };

    conditions?: Array<models.NodeCondition>;

    daemonEndpoints?: models.NodeDaemonEndpoints;

    images?: Array<models.ContainerImage>;

    nodeInfo?: models.NodeSystemInfo;

    phase?: string;

    volumesAttached?: Array<models.AttachedVolume>;

    volumesInUse?: Array<string>;

}
