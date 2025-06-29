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
 * @interface User
 */
export interface User {
    /**
     * 
     * @type {number}
     * @memberof User
     */
    id?: number;
    /**
     * 
     * @type {string}
     * @memberof User
     */
    provider?: UserProviderEnum;
    /**
     * 
     * @type {string}
     * @memberof User
     */
    name?: string;
    /**
     * 
     * @type {string}
     * @memberof User
     */
    email?: string;
    /**
     * 
     * @type {Date}
     * @memberof User
     */
    createdAt?: Date;
    /**
     * 
     * @type {Date}
     * @memberof User
     */
    lastLogin?: Date;
    /**
     * 
     * @type {number}
     * @memberof User
     */
    latitude?: number;
    /**
     * 
     * @type {number}
     * @memberof User
     */
    longitude?: number;
    /**
     * 
     * @type {Date}
     * @memberof User
     */
    locationUpdatedAt?: Date;
}


/**
 * @export
 */
export const UserProviderEnum = {
    Google: 'GOOGLE'
} as const;
export type UserProviderEnum = typeof UserProviderEnum[keyof typeof UserProviderEnum];


/**
 * Check if a given object implements the User interface.
 */
export function instanceOfUser(value: object): value is User {
    return true;
}

export function UserFromJSON(json: any): User {
    return UserFromJSONTyped(json, false);
}

export function UserFromJSONTyped(json: any, ignoreDiscriminator: boolean): User {
    if (json == null) {
        return json;
    }
    return {
        
        'id': json['id'] == null ? undefined : json['id'],
        'provider': json['provider'] == null ? undefined : json['provider'],
        'name': json['name'] == null ? undefined : json['name'],
        'email': json['email'] == null ? undefined : json['email'],
        'createdAt': json['createdAt'] == null ? undefined : (new Date(json['createdAt'])),
        'lastLogin': json['lastLogin'] == null ? undefined : (new Date(json['lastLogin'])),
        'latitude': json['latitude'] == null ? undefined : json['latitude'],
        'longitude': json['longitude'] == null ? undefined : json['longitude'],
        'locationUpdatedAt': json['locationUpdatedAt'] == null ? undefined : (new Date(json['locationUpdatedAt'])),
    };
}

export function UserToJSON(json: any): User {
    return UserToJSONTyped(json, false);
}

export function UserToJSONTyped(value?: User | null, ignoreDiscriminator: boolean = false): any {
    if (value == null) {
        return value;
    }

    return {
        
        'id': value['id'],
        'provider': value['provider'],
        'name': value['name'],
        'email': value['email'],
        'createdAt': value['createdAt'] == null ? undefined : ((value['createdAt']).toISOString()),
        'lastLogin': value['lastLogin'] == null ? undefined : ((value['lastLogin']).toISOString()),
        'latitude': value['latitude'],
        'longitude': value['longitude'],
        'locationUpdatedAt': value['locationUpdatedAt'] == null ? undefined : ((value['locationUpdatedAt']).toISOString()),
    };
}

