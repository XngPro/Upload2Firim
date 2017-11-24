#!/usr/bin/python3
#coding=utf-8

import requests
import sys


def up2Firim():
    uploadUrl = sys.argv[1]
    bundleId = sys.argv[2]
    apiToken = sys.argv[3]
    appName = sys.argv[4]
    buildNum = sys.argv[5]
    versionName = sys.argv[6]
    changeLog = sys.argv[7]
    iconPath = sys.argv[8]
    apkPath = sys.argv[9]

    iconObj = {}
    binaryObj = {}

    # 获取上传信息
    try:
        print("uploadUrl: ", uploadUrl)
        print("bundleId: ", bundleId)
        print("apiToken: ", apiToken)
        print("appName: ", appName)
        print("buildNum: ", buildNum)
        print("versionName: ", versionName)
        print("changeLog: ", changeLog)
        print("iconPath: ", iconPath)
        print("apkPath: ", apkPath)

        params = {'type': 'android',
                  'bundle_id': bundleId,
                  'api_token': apiToken}
        response = requests.post(url=uploadUrl, data=params)
        print("query_success:", response.content)
        json = response.json()
        iconObj = (json["cert"]["icon"])
        binaryObj = (json["cert"]["binary"])
    except Exception as e:
        print("query_error:", e)

    # 上传 APK
    try:
        file = {'file': open(apkPath, 'rb')}
        params = {'key': binaryObj['key'],
                  'token': binaryObj['token'],
                  'x:name': appName,
                  'x:version': versionName,
                  'x:build': buildNum,
                  'x:changelog': changeLog}
        response = requests.post(
            url=binaryObj['upload_url'],  data=params, files=file, verify=False)
        print("upload_apk_success: ", response.content)
    except Exception as e:
        print("upload_apk_error:", e)

    # 上传 Logo
    try:
        file = {'file': open(iconPath, 'rb')}
        params = {'key': iconObj['key'],
                  'token': iconObj['token']}
        response = requests.post(
            url=iconObj['upload_url'], data=params, files=file, verify=False)
        print("upload_icon_success: ", response.content)
    except Exception as e:
        print("upload_icon_error: ", e)


if __name__ == '__main__':
    up2Firim()
