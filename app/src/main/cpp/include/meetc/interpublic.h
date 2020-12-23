/***************************************************************************
 *   Copyright (C) 2017 by 95   *
 ***************************************************************************/


#ifndef INTERPUBLIC_H
#define INTERPUBLIC_H
//#include "commdef.h"
#include "deviceid_suffix.h"
//**********************************
#define INTERFACEMODULE_VERSION "1.0.1"
//**********************************

//net data status
#define DATA_STATUS_NOGET  0 //��ʾδ����
#define DATA_STATUS_HADGET 1 //��ʾ�Ѿ��������յ��ظ�
#define DATA_STATUS_TOGET  2 //��ʾ�ѷ�����δ�յ��ظ�

//�ͻ������ڵĽ��� role 
#define MEET_ROLE_IDLE		0 //û�������ͺ�̨
#define MEET_ROLE_COMMON	1 //�������
#define MEET_ROLE_SECRETARY 2 //�����̨

#define MEET_MAX_VOTENUM	6 //���ͶƱѡ����

#define IS_DEVMEETDBSERVER(x)     (((x) & DEVICE_MEET_ID_MASK) == DEVICE_MEET_DB) //���豸ID�Ƿ�������ݿ�
#define IS_DEVMEETSERVICE(x)     (((x) & DEVICE_MEET_ID_MASK) == DEVICE_MEET_SERVICE) //���豸ID�Ƿ�����ˮ�����ն�
#define IS_DEVPROJECTIVE(x)     (((x) & DEVICE_MEET_ID_MASK) == DEVICE_MEET_PROJECTIVE) //���豸ID�Ƿ�ͶӰ�豸
#define IS_DEVVIDEOCAPTURE(x)     (((x) & DEVICE_MEET_ID_MASK) == DEVICE_MEET_CAPTURE) //���豸ID�Ƿ����ɼ��豸
#define IS_MEET_CLIENT(x)     (((x) & DEVICE_MEET_ID_MASK) == DEVICE_MEET_CLIENT) //���豸ID�ǻ���PC�ն��豸
#define IS_MEET_ONEKEYSHARE(x)     (((x) & DEVICE_MEET_ID_MASK) == DEVICE_MEET_ONEKEYSHARE) //���豸ID�ǻ���PC�ն��豸
#define IS_MEET_PUBLISH(x)     (((x) & DEVICE_MEET_ID_MASK) == DEVICE_MEET_PUBLISH) //���豸ID�ǻ��鷢���豸
#define IS_MEET_PHPCLIENT(x)     (((x) & DEVICE_MEET_PHPCLIENT) == DEVICE_MEET_PHPCLIENT) //���豸ID��PHP��ת�����豸
#endif
