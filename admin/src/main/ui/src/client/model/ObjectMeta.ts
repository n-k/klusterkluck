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

export interface ObjectMeta {
    annotations?: { [key: string]: string; };

    clusterName?: string;

    creationTimestamp?: string;

    deletionGracePeriodSeconds?: number;

    deletionTimestamp?: string;

    finalizers?: Array<string>;

    generateName?: string;

    generation?: number;

    labels?: { [key: string]: string; };

    name?: string;

    namespace?: string;

    ownerReferences?: Array<models.OwnerReference>;

    resourceVersion?: string;

    selfLink?: string;

    uid?: string;

}
