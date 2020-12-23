
#ifndef _WALLET_BASE_MACROPUBLIC_H
#define _WALLET_BASE_MACROPUBLIC_H

//encodemode
#define DEVICE_INVITECHAT_SYSDEFAULT        0xff //ʹ��ϵͳĬ��
#define DEVICE_INVITECHAT_ENCODEMODE_HIGH   0 //�ߴ���
#define DEVICE_INVITECHAT_ENCODEMODE_MIDDLE 1 //������
#define DEVICE_INVITECHAT_ENCODEMODE_LOW    2 //�ʹ���

#define MAX_RES_NUM 12 //���������Դ��

#ifndef MEDIA_FILETYPE_AUDIO
//����
#define MEDIA_FILETYPE_AUDIO    0x00000000 //��Ƶ
#define MEDIA_FILETYPE_VIDEO	0x20000000 //��Ƶ
#define MEDIA_FILETYPE_RECORD	0x40000000 //¼��
#define MEDIA_FILETYPE_PICTURE	0x60000000 //ͼƬ
#define MEDIA_FILETYPE_UPDATE	0xe0000000 //����
//#define MEDIA_FILETYPE_TEMP		0x80000000 //��ʱ�ļ�
#define MEDIA_FILETYPE_OTHER	0xa0000000 //�����ļ�
#define MAINTYPEBITMASK			0xe0000000
//С��
#define MEDIA_FILETYPE_PCM		0x01000000	//PCM�ļ�
#define MEDIA_FILETYPE_MP3		0x02000000	//MP3�ļ�
#define MEDIA_FILETYPE_ADPCM	0x03000000	//WAV�ļ�
#define MEDIA_FILETYPE_FLAC		0x04000000	//FLAC�ļ�
#define MEDIA_FILETYPE_MP4		0x07000000  //MP4�ļ�
#define MEDIA_FILETYPE_MKV		0x08000000  //MKV�ļ�
#define MEDIA_FILETYPE_RMVB		0x09000000  //RMVB�ļ�
#define MEDIA_FILETYPE_RM		0x0a000000  //RM�ļ�
#define MEDIA_FILETYPE_AVI		0x0b000000  //AVI�ļ�
#define MEDIA_FILETYPE_BMP		0x0c000000  //bmp�ļ�
#define MEDIA_FILETYPE_JPEG		0x0d000000  //jpeg�ļ�
#define MEDIA_FILETYPE_PNG		0x0e000000  //png�ļ�
#define MEDIA_FILETYPE_OTHERSUB	0x10000000  //�����ļ�
#define SUBTYPEBITMASK			0x1f000000

#define MEDIA_FILETYPE_NET		0x00000000 //�����ļ�
#define MEDIA_FILETYPE_TEMP		0x00800000 //��ʱ�ļ�
#define MEDIA_FILETYPE_LOCAL	0x00c00000 //�����ļ�
#define SAVETYPEBITMASK			0x00c00000

#define MEDIATYPEBITCOUNT       12		   //ý������ռ�ö���λ
#define MEDIATOTALTYPE			0xffc00000 //ý������
#endif

#define DEVICECHAT_AUDIO_B_RESINDEX 9 //�Խ�Զ����Ƶ������ԴID
#define DEVICECHAT_VIDEO_B_RESINDEX 10 //�Խ�Զ�˶���Ƶ������ԴID
#define DEVICECHAT_VIDEO_A_RESINDEX 11 //�Խ�������Ƶ������ԴID


#define VIDEO_START_RESID 2 //������Ƶ��ʼ��ԴID
#define AUDIO_START_RESID 6//������ƵƵ��ʼ��ԴID

#define NET_EXCHANGE_TYPE 0x1e //���ݿ��̨��Ե㴫������

#define RES_RECORDVIDEOSTREAM_INDEX  0 //��Ƶ������
#define RES_SCREENSTREAM_INDEX		 1 //��Ļ������

#define NETINT32DATA_ATTRIBID				47 //��ʶ��ǰNETBLOBDATA_ATTRIBID�Ķ����������Ƿ��͸��Ǹ��豸�ļĴ���ID
#define MCSERVER_NETBLOBDATA_ATTRIBID		48 //��̨������   ����������ʹ�õļĴ���ID
#define COMMONCLIENT_NETBLOBDATA_ATTRIBID   49 //��ͨ�����ն� ����������ʹ�õļĴ���ID
#define COMMONCLIENT_EXCHANGE_NETBLOBDATA_ATTRIBID   48 //��ͨ�����ն� ֮����ת����������ʹ�õļĴ���ID ,ע:����ͷʹ�÷�������Ӧ��ͷ

#define  USE_DEVICE_EXCHANGE  1 //�����豸��Ե㽻������
#define  CTOS_EXCHANGE		  0 //Ϊ1��ʾ�ɿͻ��˷��𽻻� 0��ʾ�ӷ���˷��𽻻� 

#define MEET_SUPPORTOLDANDNEW 0 //Ϊ0��ʾǿ��ƥ�����ʹ�õ��豸����

#define MEETPUSHSTREAM_FLAG_MIC		   0x00000001 //��Ƶ�ɼ���
#define MEETPUSHSTREAM_FLAG_SCREEN	   0x00000002 //��Ļ��
#define MEETPUSHSTREAM_FLAG_USB	       0x00000004 //usb��
#define MEETPUSHSTREAM_FLAG_HKIPC      0x00000008 //usb��

//SystemAttrib -- >dns2
#define OCCUPYSYSTEMATTRIBDNSNUM    3 //ռ���豸63�żĴ����ı���dns2���ֽ���
#define MEETDEVICE_LIFTGROUPID		0 //��������ID���� dns2[0]
#define MEETDEVICE_MICLIFTGROUPID	1 //������Ͳ��ID���� dns2[1]

#define MEETDEVICE_FLAG			    2 //�����豸��һЩ��־ dns2[2]
#define MEETDEVICE_FLAG_DIRECTENTER 0x01 //��ǩ���������
#define MEETDEVICE_FLAG_OPENOUTSIDE 0x02 //ʹ���ⲿ������ĵ�
#define MEETDEVICE_FLAG_GUESTMODE	0x04 //ʹ������ģʽ-����ʱ����ѡ��λ���ǩ��
#define MEETDEVICE_FLAG_WELCOMEPAGE	0x08 //��ʾ��ӭ����-���豸�л��鲢�Ұ���Աʱ�����ֱ��ǩ��������飬�����������ʾ��ӭ����

//����ϵͳ�ϴ��ļ�����
#define MEET_FILEATTRIB_BACKGROUND		0x10000 //�����ļ�
#define MEET_FILEATTRIB_TABLECARD		0x20000 //���Ʊ����ļ�
#define MEET_FILEATTRIB_DEVICEUPDATE	0x40000 //�豸�����õ��ļ�
#define MEET_FILEATTRIB_PUBLISH		    0x80000 //���鷢��
#define MEET_FILEATTRIB_WELCOMEPAGE		0x100000 //���黶ӭ����

#endif
