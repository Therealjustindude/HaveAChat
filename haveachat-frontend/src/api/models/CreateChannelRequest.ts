/* tslint:disable */
/* eslint-disable */
/**
 * OpenAPI definition
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: v0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

import { mapValues } from '../runtime';
/**
 * 
 * @export
 * @interface CreateChannelRequest
 */
export interface CreateChannelRequest {
    /**
     * 
     * @type {string}
     * @memberof CreateChannelRequest
     */
    name?: string;
    /**
     * 
     * @type {string}
     * @memberof CreateChannelRequest
     */
    description?: string;
    /**
     * 
     * @type {boolean}
     * @memberof CreateChannelRequest
     */
    privateChannel?: boolean;
    /**
     * 
     * @type {string}
     * @memberof CreateChannelRequest
     */
    type?: CreateChannelRequestTypeEnum;
}


/**
 * @export
 */
export const CreateChannelRequestTypeEnum = {
    Dm: 'DM',
    Group: 'GROUP',
    Course: 'COURSE'
} as const;
export type CreateChannelRequestTypeEnum = typeof CreateChannelRequestTypeEnum[keyof typeof CreateChannelRequestTypeEnum];


/**
 * Check if a given object implements the CreateChannelRequest interface.
 */
export function instanceOfCreateChannelRequest(value: object): value is CreateChannelRequest {
    return true;
}

export function CreateChannelRequestFromJSON(json: any): CreateChannelRequest {
    return CreateChannelRequestFromJSONTyped(json, false);
}

export function CreateChannelRequestFromJSONTyped(json: any, ignoreDiscriminator: boolean): CreateChannelRequest {
    if (json == null) {
        return json;
    }
    return {
        
        'name': json['name'] == null ? undefined : json['name'],
        'description': json['description'] == null ? undefined : json['description'],
        'privateChannel': json['privateChannel'] == null ? undefined : json['privateChannel'],
        'type': json['type'] == null ? undefined : json['type'],
    };
}

export function CreateChannelRequestToJSON(json: any): CreateChannelRequest {
    return CreateChannelRequestToJSONTyped(json, false);
}

export function CreateChannelRequestToJSONTyped(value?: CreateChannelRequest | null, ignoreDiscriminator: boolean = false): any {
    if (value == null) {
        return value;
    }

    return {
        
        'name': value['name'],
        'description': value['description'],
        'privateChannel': value['privateChannel'],
        'type': value['type'],
    };
}

