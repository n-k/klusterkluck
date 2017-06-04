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

export interface ServiceSpec {
    clusterIP?: string;

    deprecatedPublicIPs?: Array<string>;

    externalIPs?: Array<string>;

    externalName?: string;

    loadBalancerIP?: string;

    loadBalancerSourceRanges?: Array<string>;

    ports?: Array<models.ServicePort>;

    selector?: { [key: string]: string; };

    sessionAffinity?: string;

    type?: string;

}
