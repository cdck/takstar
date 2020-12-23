#ifndef MEETINTERFACE_TYPE_H
#define MEETINTERFACE_TYPE_H

#include "base.h"
#include "interpublic.h"
#include "owbase.h"
//#include "commdef.h"
#pragma pack(push, 4)

// #ifdef __cplusplus
// extern "C" {
// #endif

//triggeruserval
#define EXCEC_USERDEF_FLAG_FORCEOPEN					0x00000001 //��λΪ1��ʾǿ������,Ϊ0��ʾѯ��
#define MEETFILE_PUSH_FLAG_FORCEMODE					0x00000002 //��λΪ1��ʾǿ��ģʽ �������Լ�ֹͣ,Ϊ0��ʾ����ģʽ
#define EXCEC_USERDEF_FLAG_NOCREATEWINOPER				0x00000004 //�������´��ڲ�����־
#define EXCEC_USERDEF_FLAG_NOSTREAMRECORD				0x00000008 //��������¼��������־
#define EXCEC_USERDEF_FLAG_SCREENSHARE					0x00000010 //ͬ������
#define EXCEC_USERDEF_FLAG_SETPOSMODE					0x00000020 //����λ�õ��� 0�ٷֱ� Ϊ1��ʾ��ʱ��������룩

//error
#define ERROR_MEET_INTERFACE_NULL	 0  //�����ɹ�����������Ϊ�գ��Ӳ�������
#define ERROR_MEET_INTERFACE_NOTINIT -1 //δ��ʼ��
#define ERROR_MEET_INTERFACE_MEMORY  -2 //�ڴ����ʧ��
#define ERROR_MEET_INTERFACE_NOSPPORTTYPE  -3 //��֧�ֵ�����
#define ERROR_MEET_INTERFACE_NOSPPORTMETHOD  -4 //��֧�ֵ����󷽷�
#define ERROR_MEET_INTERFACE_DOWNLOAD  -5 //����ʧ��
#define ERROR_MEET_INTERFACE_NETSEND -6 //����ʧ��
#define ERROR_MEET_INTERFACE_NOREADY -7 //����δ׼����,�������ڵȴ����ݷ���
#define ERROR_MEET_INTERFACE_NOFIND -8 //�Ҳ���
#define ERROR_MEET_INTERFACE_NOSPPORTPROPERTY -9 //��֧�ֵ�����
#define ERROR_MEET_INTERFACE_UPPONMAXVALUE -10 //�������ֵ
#define ERROR_MEET_INTERFACE_ZERO -11 //����Ϊ0
#define ERROR_MEET_INTERFACE_PARAMETER -12 //��������
#define ERROR_MEET_INTERFACE_DATA -13 //���ݴ���
#define ERROR_MEET_INTERFACE_UNAUTHORIZED -14 //����δ��Ȩʹ��
#define ERROR_MEET_INTERFACE_NOCONNECT -15 // ���ŷ�����δ��Ӧ
#define ERROR_MEET_INTERFACE_SOCKET    -16 // SOCKET����ʧ��
#define ERROR_MEET_INTERFACE_NAMEORPWD -17 // �û������������
#define ERROR_MEET_INTERFACE_SERVERERR -18 // ���ʷ����쳣
#define ERROR_MEET_INTERFACE_NOSMSCNT  -19 // ����
#define ERROR_MEET_INTERFACE_OPENFILEERROR  -20 // �ļ���ʧ��

//method
#define METHOD_MEET_INTERFACE_QUERY     1
#define METHOD_MEET_INTERFACE_ADD		2
#define METHOD_MEET_INTERFACE_MODIFY    3
#define METHOD_MEET_INTERFACE_DEL		4
#define METHOD_MEET_INTERFACE_SET		6
#define METHOD_MEET_INTERFACE_GETSIZE   7
#define METHOD_MEET_INTERFACE_NOTIFY    8
#define METHOD_MEET_INTERFACE_CLEAR     9
#define METHOD_MEET_INTERFACE_LOGON     10
#define METHOD_MEET_INTERFACE_PUSH      11
#define METHOD_MEET_INTERFACE_ASK       12
#define METHOD_MEET_INTERFACE_REJECT    13
#define METHOD_MEET_INTERFACE_EXIT      14
#define METHOD_MEET_INTERFACE_OPEN      15
#define METHOD_MEET_INTERFACE_CLOSE     16
#define METHOD_MEET_INTERFACE_ENTER     17
#define METHOD_MEET_INTERFACE_PAGEQUERY     18
#define METHOD_MEET_INTERFACE_MODIFYNAME    19
#define METHOD_MEET_INTERFACE_MODIFYINFO    20
#define METHOD_MEET_INTERFACE_MODIFYSTATUS    21
#define METHOD_MEET_INTERFACE_SINGLEQUERYBYID     22
#define METHOD_MEET_INTERFACE_SINGLEQUERYBYNAME   23
#define METHOD_MEET_INTERFACE_CONTROL   24
#define METHOD_MEET_INTERFACE_SAVE   25
#define METHOD_MEET_INTERFACE_REFRESH   26
#define METHOD_MEET_INTERFACE_COMPLEXQUERY     27 //���ϲ�ѯ
#define METHOD_MEET_INTERFACE_COMPLEXPAGEQUERY     28 //���ϰ�ҳ��ѯ
#define METHOD_MEET_INTERFACE_START   29
#define METHOD_MEET_INTERFACE_STOP   30
#define METHOD_MEET_INTERFACE_PLAY   31
#define METHOD_MEET_INTERFACE_PAUSE   32
#define METHOD_MEET_INTERFACE_MOVE   33
#define METHOD_MEET_INTERFACE_ZOOM   34 //���Ų��� 
#define METHOD_MEET_INTERFACE_BACK   35 //
#define METHOD_MEET_INTERFACE_REQUEST   36 //
#define METHOD_MEET_INTERFACE_BROADCAST   37 //
#define METHOD_MEET_INTERFACE_QUERYPROPERTY   38 //��ѯ����
#define METHOD_MEET_INTERFACE_SETPROPERTY   39 //��������
#define METHOD_MEET_INTERFACE_DUMP   40 //����
#define METHOD_MEET_INTERFACE_REQUESTPUSH   41 //ѯ������
#define METHOD_MEET_INTERFACE_DELALL		42
#define METHOD_MEET_INTERFACE_SEND		43
#define METHOD_MEET_INTERFACE_DETAILINFO		44
#define METHOD_MEET_INTERFACE_INIT		45
#define METHOD_MEET_INTERFACE_DESTORY		46
#define METHOD_MEET_INTERFACE_MAKEVIDEO		47
#define METHOD_MEET_INTERFACE_SUBMIT		48
#define METHOD_MEET_INTERFACE_PUBLIST		49
#define METHOD_MEET_INTERFACE_CACHE		50 //����
#define METHOD_MEET_INTERFACE_CHILDRED		51 //�Ӽ�����
#define METHOD_MEET_INTERFACE_ALLCHILDRED		52 //�����Ӽ�����
#define METHOD_MEET_INTERFACE_RESPONSE   53 //
#define METHOD_MEET_INTERFACE_REQUESTTOMANAGE   54 //
#define METHOD_MEET_INTERFACE_RESPONSETOMANAGE   55 //
#define METHOD_MEET_INTERFACE_REQUESTPRIVELIGE   56 //
#define METHOD_MEET_INTERFACE_RESPONSEPRIVELIGE   57 //
#define METHOD_MEET_INTERFACE_UPDATE   58 //
#define METHOD_MEET_INTERFACE_TEXTMSG   59 //
#define METHOD_MEET_INTERFACE_REBOOT    60 //
#define METHOD_MEET_INTERFACE_RESINFO   61 //
#define METHOD_MEET_INTERFACE_LOCATE    62 //
#define METHOD_MEET_INTERFACE_REQUESTINVITE    63 //
#define METHOD_MEET_INTERFACE_RESPONSEINVITE    64 //
#define METHOD_MEET_INTERFACE_EXITCHAT    65 //
#define METHOD_MEET_INTERFACE_REMOTESET 66
//type
#define TYPE_MEET_INTERFACE_TIME 1 //ƽ̨ʱ�� -- ��Ƶ�ص�
#define TYPE_MEET_INTERFACE_READY 2 //ƽ̨��ʼ�����
#define TYPE_MEET_INTERFACE_DOWNLOAD 3 //ƽ̨���� -- ��Ƶ�ص�
#define TYPE_MEET_INTERFACE_UPLOAD 4 //ƽ̨�ϴ� -- ��Ƶ�ص�
#define TYPE_MEET_INTERFACE_MEDIAPLAYPOSINFO 5 //ƽ̨���Ž���֪ͨ -- ��Ƶ�ص�
#define TYPE_MEET_INTERFACE_DEVICEINFO 6 //�豸
#define TYPE_MEET_INTERFACE_DEFAULTURL 7 //������ҳ
#define TYPE_MEET_INTERFACE_ADMIN	 8 //����Ա
#define TYPE_MEET_INTERFACE_DEVICECONTROL 9 //�豸����
#define TYPE_MEET_INTERFACE_PEOPLE  10 //������Ա��Ϣ
#define TYPE_MEET_INTERFACE_MEMBER  11 //�λ���Ա��Ϣ
#define TYPE_MEET_INTERFACE_MEMBERGROUP  12 //�λ���Ա����
#define TYPE_MEET_INTERFACE_MEMBERGROUPITEM  13 //�λ���Ա������Ա
#define TYPE_MEET_INTERFACE_DEVICEFACESHOW  14 //�豸������Ϣ
#define TYPE_MEET_INTERFACE_ROOM  15 //�᳡
#define TYPE_MEET_INTERFACE_ROOMDEVICE  16 //�᳡�豸
#define TYPE_MEET_INTERFACE_FILEPUSH  17 //�ļ�����
#define TYPE_MEET_INTERFACE_REQUESTSTREAMPUSH  18 //����������
#define TYPE_MEET_INTERFACE_STREAMPUSH  19 //������
#define TYPE_MEET_INTERFACE_MEETINFO  20 //������Ϣ
#define TYPE_MEET_INTERFACE_MEETAGENDA  21 //�������
#define TYPE_MEET_INTERFACE_MEETBULLET  22 //���鹫��
#define TYPE_MEET_INTERFACE_MEETDIRECTORY  23 //����Ŀ¼
#define TYPE_MEET_INTERFACE_MEETDIRECTORYFILE  24 //����Ŀ¼�ļ�
#define TYPE_MEET_INTERFACE_MEETDIRECTORYRIGHT  25 //����Ŀ¼Ȩ��
#define TYPE_MEET_INTERFACE_MEETVIDEO  26 //������Ƶ
#define TYPE_MEET_INTERFACE_MEETTABLECARD  27 //����˫����ʾ
#define TYPE_MEET_INTERFACE_MEETSEAT  28 //������λ
#define TYPE_MEET_INTERFACE_MEETIM  29 //���齻��
#define TYPE_MEET_INTERFACE_MEETONVOTING  30 //���鷢��ͶƱ
#define TYPE_MEET_INTERFACE_MEETVOTEINFO 31 //����ͶƱ��Ϣ
#define TYPE_MEET_INTERFACE_MEETVOTESIGNED 32 //����ͶƱ����Ϣ
#define TYPE_MEET_INTERFACE_MANAGEROOM 33 //�������᳡
#define TYPE_MEET_INTERFACE_MEETSIGN 34 //����ǩ��
#define TYPE_MEET_INTERFACE_WHITEBOARD 35 //����װ�
#define TYPE_MEET_INTERFACE_FUNCONFIG  36 //���鹦������
#define TYPE_MEET_INTERFACE_MEMBERPERMISSION  37 //�λ���ԱȨ����Ϣ
#define TYPE_MEET_INTERFACE_PEOPLEGROUP  38 //������Ա����
#define TYPE_MEET_INTERFACE_PEOPLEGROUPITEM  39 //������Ա������Ա
#define TYPE_MEET_INTERFACE_SCREENMOUSECONTROL  40 //��Ļ������
#define TYPE_MEET_INTERFACE_SCREENKEYBOARDCONTROL  41 //��Ļ���̿���
#define TYPE_MEET_INTERFACE_DEVICEMEETSTATUS  42 //�豸��ǰ�����һЩ��Ϣ
#define TYPE_MEET_INTERFACE_DEVICEOPER  43 //�豸������Ϣ
#define TYPE_MEET_INTERFACE_DBSERVERERROR 44 //���ݿ��̨�ظ�������Ϣ
#define TYPE_MEET_INTERFACE_MEDIAPLAY  45 //ý�岥��
#define TYPE_MEET_INTERFACE_STREAMPLAY 46 //������
#define TYPE_MEET_INTERFACE_STOPPLAY 47 //ֹͣ����
#define TYPE_MEET_INTERFACE_MEMBERCOLOR  48 //�λ���Ա�װ���ɫ
#define TYPE_MEET_INTERFACE_MEETCONTEXT  49 //������
#define TYPE_MEET_INTERFACE_MEETCLEAR  50 //��������,������л���,��������Ҳ֧�ֵ���
#define TYPE_MEET_INTERFACE_MEETSTATISTIC  51 //����ͳ��
#define TYPE_MEET_INTERFACE_MEETFACECONFIG  52 //��������
#define TYPE_MEET_INTERFACE_MEETSENDMAIL  53 //�ʼ�����
#define TYPE_MEET_INTERFACE_FILESCORE     54    //�����ļ��������
#define TYPE_MEET_INTERFACE_FILEEVALUATE  55  //�����ļ��������
#define TYPE_MEET_INTERFACE_MEETEVALUATE  56   //�����������
#define TYPE_MEET_INTERFACE_SYSTEMLOG     57 //����Ա������־���
#define TYPE_MEET_INTERFACE_DEVICEVALIDATE 58 //�豸IDУ��
#define TYPE_MEET_INTERFACE_SYSTEMFUNCTIONLIMIT 59 //ƽ̨��������
#define TYPE_MEET_INTERFACE_PUBLICINFO 60 //ȫ���ִ�
#define TYPE_MEET_INTERFACE_FILESCOREVOTE 61 //�Զ����ļ�����ͶƱ
#define TYPE_MEET_INTERFACE_PDFWHITEBOARD 62 //PDF�ļ�����ͬʱ��д
#define TYPE_MEET_INTERFACE_FILESCOREVOTESIGN  63 //�Զ����ļ�����ͶƱ������Ϣ
#define TYPE_MEET_INTERFACE_ZKIDENTIFY  64 //�п�������֤
#define TYPE_MEET_INTERFACE_SMSSERVICE  65 // ���ŷ���
#define TYPE_MEET_INTERFACE_UPDATE      66 // �������
#define TYPE_MEET_INTERFACE_TOPIC  67 //��������
#define TYPE_MEET_INTERFACE_TOPICGROUP  68 // ��������μӵ�λ
#define TYPE_MEET_INTERFACE_TOPICPERMINSSION      69 // ��������Ȩ��
#define TYPE_MEET_INTERFACE_MEETTASK      70 // ���鷢������

//ע���������е��ַ�����Լ��Ϊutf8����
#define DEFAULT_NAME_MAXLEN   48 //Ĭ�����Ƴ���
#define DEFAULT_VOTECONTENT_LENG 200//Ĭ��ͶƱ���������ַ�������
#define DEFAULT_VOTEITEM_LENG    60//Ĭ��ͶƱѡ�������ַ�������
#define DEFAULT_DESCRIBE_LENG    100//Ĭ�������ַ�������
#define DEFAULT_SHORT_DESCRIBE_LENG 40//Ĭ�������ַ�������
#define DEFAULT_PASSWORD_LENG 40 //Ĭ�����볤��
#define DEFAULT_FILENAME_LENG 150 //Ĭ���ļ�������
#define MEET_MAX_TASKNAMELEN  80 //�����������Ƴ���

//font align flag ������뷽ʽ
#define MEET_FONTFLAG_LEFT			0x0001 //�����
#define MEET_FONTFLAG_RIGHT			0x0002 //�Ҷ���
#define MEET_FONTFLAG_HCENTER		0x0004 //ˮƽ����
#define MEET_FONTFLAG_TOP			0x0008 //�϶���
#define MEET_FONTFLAG_BOTTOM		0x0010 //�¶���
#define MEET_FONTFLAG_VCENTER		0x0020 //��ֱ����

//////////////////////////////////////////////////////////////////////////
typedef struct
{
	int32u totalbufsize;//�������õ����ڴ��С
	int32u type;//��������
	int32u method;//���󷽷�
	int32u meetingid;//����ID �����ݶ�Ӧ�Ļ���ID
}Type_HeaderInfo, *pType_HeaderInfo;

//ƽ̨��������ݳ�ʼ���ɹ�
//type:TYPE_MEET_INTERFACE_READY
//method: notify
//callback
typedef struct
{
	Type_HeaderInfo hdr;
	int32u			areaid;//�����ϵ����������ID
}Type_Ready, *pType_Ready;

//ƽ̨�豸У��
//type:TYPE_MEET_INTERFACE_DEVICEVALIDATE
//method: notify
//callback
typedef struct
{
	Type_HeaderInfo hdr;

	//�ο�basemacro.h�ĺ궨��
	int32u      valflag;
	int32u      val[32];
	int64u		user64bitdef[2];
}Type_DeviceValidate, *pType_DeviceValidate;

//ƽ̨��½�����뷵��  -- ��½ʧ�ܲŻ�֪ͨ
//type:TYPE_MEET_INTERFACE_READY
//method: logon
//callback
typedef struct
{
	Type_HeaderInfo hdr;

	int32u      errcode; //�μ�commdef.h RETURN_ERROR_NONE����
}Type_LogonError, *pType_LogonError;

#define SYSTEMFUNCTIONLIMIT_PROPERTY_ISENABLE 0 //��ѯ�����Ƿ���� query,parameter(����Ĺ���ID �μ�systemfuncion.h�Ĺ���ID����),propertyval(�����Ƿ����1=����,0=������)
#define SYSTEMFUNCTIONLIMIT_PROPERTY_TOTALNUM 1 //��ѯ���� query,parameter(0),returnval(��������)

//ƽ̨��������
//type:TYPE_MEET_INTERFACE_SYSTEMFUNCTIONLIMIT
//method: queryproperty
//call
typedef struct
{
	Type_HeaderInfo hdr;

	int32u propertyid;
	int32u parameter;
	int32u propertyval;
}Type_SystemFuntionLimitsProperty, *pType_SystemFuntionLimitsProperty;

//ƽ̨��������
//type:TYPE_MEET_INTERFACE_SYSTEMFUNCTIONLIMIT
//method: query queryproperty
//call
typedef struct
{
	Type_HeaderInfo hdr;
	
	int      num;
	//int32u      functionids[num];//�μ�systemfuncion.h�Ĺ���ID����
}Type_SystemFuntionLimits, *pType_SystemFuntionLimits;

//ƽ̨ʱ��
//type:TYPE_MEET_INTERFACE_TIME
//method: notify
//callback
typedef struct
{
	Type_HeaderInfo hdr;
	int64u			usec;//����ʱ�䣬ÿ��һ�� ��λ��΢��
}Type_Time, *pType_Time;

//��������
#define MEET_CACEH_FLAG_FORCE	     0x00000001 //ǿ�ƻ���

//call
//type:��Ҫ�������ݵ����
//method: cache 
//����л����򷵻�ERROR_MEET_INTERFACE_NULL
typedef struct
{
	Type_HeaderInfo hdr;
	int32u      cacheflag;//

	//id�÷����� eg:��Ҫ����Ŀ¼id=0��ʾ��������Ŀ¼��Ϣ(������Ŀ¼����ļ�),��id=1ʱ��ʾ�����Ŀ¼����ļ�,���id=0��֧����᷵��ERROR_MEET_INTERFACE_PARAMETER
	int32u		id;
}Type_MeetCacheOper, *pType_MeetCacheOper;

//��ҳ��ѯ
//call
//type:��Ҫ��ѯ�����ݵ����
//method: pagequery Ӧ�÷�����
typedef struct
{
	Type_HeaderInfo hdr;
	int			pageindex;//����ҳ ��0 ��ʼ
	int			pagenum;  //ÿҳ����
	int32u      idval;//���ڷ���ʹ��,�磺��ѯָ�����е���Ա,������д����id
}TypePageReqQueryInfo, *pTypePageReqQueryInfo;

//��ҳ��ѯ���ؽṹͷ��
//callreturn
//type:��Ҫ��ѯ�����ݵ����
//method: pagequery �ӿڷ�����
typedef struct
{
	Type_HeaderInfo hdr;
	int			pageindex;//��ʼ���� ��0 ��ʼ
	int			pagenum;//ÿҳ����
	int			itemnum;//��ǰҳ���� ,�������ݲ�ͬ�ò�ͬ�Ľ���
	int			totalnum;//������
	int32u      idval;//���ڷ���ʹ��,�磺��ѯָ�����е���Ա,������д����id
}TypePageResQueryInfo, *pTypePageResQueryInfo;

//ָ��ID��ѯ ���ؽ���� query ����
//call
//type:��Ҫ��ѯ�����ݵ����
//method: SINGLEQUERYBYID Ӧ�÷�����
typedef struct
{
	Type_HeaderInfo hdr;
	int32u id;
}TypeQueryInfoByID, *pTypeQueryInfoByID;

//ָ�����Ʋ�ѯ ���ؽ���� query ����
//method: SINGLEQUERYBYNAME Ӧ�÷�����
typedef struct
{
	Type_HeaderInfo hdr;
	char name[DEFAULT_FILENAME_LENG];
}TypeQueryInfoByName, *pTypeQueryInfoByName;

//ָ��˫��ID��ѯ ���ؽ���� query ����
//method: SINGLEQUERYBYID Ӧ�÷�����
typedef struct
{
	Type_HeaderInfo hdr;
	int32u id1;
	int32u id2;
}TypeQueryInfoByDoubleID, *pTypeQueryInfoByDoubleID;

//ָ��˫��ID��ѯ ���ؽ���� query ����
//method: SINGLEQUERYBYNAME Ӧ�÷�����
typedef struct
{
	Type_HeaderInfo hdr;
	int32u id1;
	char name[DEFAULT_FILENAME_LENG];
}TypeDoubleQueryInfoByName, *pTypeDoubleQueryInfoByName;

//��ȡ��С
//callreturn
//type:��Ҫ��ѯ�Ĵ�С�����
//method: METHOD_MEET_INTERFACE_GETSIZE
typedef struct
{
	Type_HeaderInfo hdr;
	int32u      idval;//���ڷ���ʹ��,�磺��ѯָ�����е���Ա,������д����id

	int32u		size;//���صĴ�С
}Type_MeetQuerySize, *pType_MeetQuerySize;

//////////////////////////////////////////////////////////////////////////
//�豸�Ļ�����Ϣ
//callback
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;
	int32u  deviceid;
	int		attribid;// 0:net status  50:res status  63:base info
}Type_MeetDeviceBaseInfo, *pType_MeetDeviceBaseInfo;

//MemState �豸��ǰ�����Ľ���״̬
#define MemState_MainFace  0 //����������
#define MemState_MemFace   1 //�λ���Ա����
#define MemState_AdminFace 2 //��̨�������
#define MemState_VoteFace  3 //ͶƱ����

//ƽ̨�豸
//playstatus
#define DEVICE_PLAYSTATUS_IDLE 0
#define DEVICE_PLAYSTATUS_MEDIA 1
#define DEVICE_PLAYSTATUS_STREAM 2
#define DEVICE_PLAYSTATUS_TALK 3

#define DEVICE_NAME_MAXLEN   128
#define DEVICE_IPADDR_MAXLEN 64

#define MAX_DEVICEIPADDR_NUM 2 //�豸����IP��ַ��Ϣ��
typedef struct
{
	char   ip[DEVICE_IPADDR_MAXLEN];
	int16u port;
}SubItem_DeviceIpAddrInfo, *pSubItem_DeviceIpAddrInfo;

typedef struct
{
	int8u  playstatus;
	int32u triggerId;
	int32u val; //status =DEVICE_PLAYSTATUS_MEDIA mediaid��DEVICE_PLAYSTATUS_STREAM�豸ID��DEVICE_PLAYSTATUS_TALK�Խ��豸ID
	int32u val2; //status =DEVICE_PLAYSTATUS_MEDIA 0������,1��ͣ|DEVICE_PLAYSTATUS_STREAM,DEVICE_PLAYSTATUS_TALK����ͨ��
	int32u createdeviceid; //�����������豸ID
}SubItem_DeviceResInfo, *pSubItem_DeviceResInfo;
typedef struct
{
	int32u devcieid;
	char   devname[DEVICE_NAME_MAXLEN];
	int32u netstate;//0=���� 1=����
	int32u version;////soft(high16):hard(low16) 

	SubItem_DeviceIpAddrInfo ipinfo[MAX_DEVICEIPADDR_NUM];
	SubItem_DeviceResInfo    resinfo[MAX_RES_NUM];

	int32u	facestate;//����״̬ �μ�MemState_MainFace ����
	int32u  memberid;//��ǰ��Ա
	int32u  meetingid;//��ǰ����
	int8u   liftgroupres[2];//0��������ID 1������Ͳ��ID
	int8u   deviceflag;//�����豸���� �μ�owbash.h MEETDEVICE_FLAG ����
}Item_DeviceDetailInfo, *pItem_DeviceDetailInfo;

//type:TYPE_MEET_INTERFACE_DEVICEINFO
//method: query/complexquery����
typedef struct
{
	Type_HeaderInfo hdr;
	int32u			devnum;
}Type_DeviceDetailInfo, *pType_DeviceDetailInfo;

#define DEVICE_COMPLEXQUERY_DEVICETYPE 0x00000001 //devcietype mask��Ч
#define DEVICE_COMPLEXQUERY_DEVICENAME 0x00000002 //devname ��Ч
#define DEVICE_COMPLEXQUERY_DEVICESTAT 0x00000004 //netstate ��Ч
#define DEVICE_COMPLEXQUERY_DEVICEMEET 0x00000008 //meetingid ��Ч
//��ϲ�ѯ�豸
//call
//type:TYPE_MEET_INTERFACE_DEVICEINFO
//method: complexquery--Type_DeviceDetailInfo����
typedef struct
{
	Type_HeaderInfo hdr;
	int32u			queryflag;//��ѯ��־,����ָ����ѯ����
	int32u			devcietype;//��mask��Ͽ��Բ�ѯ���ϵ��豸 devcieid & mask == devcietype �μ�deviceid_suffix.h
	int32u			mask;//0xff00000
	char			devname[DEVICE_NAME_MAXLEN];//ģ����ѯ,���غ����ַ������豸
	int32u		    netstate;//0=���� 1=����
	int32u			meetingid;//���ڸû���ID���豸
}Type_DeviceComplexQuery, *pType_DeviceComplexQuery;

#define DEVICEQUERYSTREAM_DEVICETYPE 0x00000001 //devcietype mask��Ч
#define DEVICEQUERYSTREAM_DEVICESTAT 0x00000002 //netstate ��Ч
#define DEVICEQUERYSTREAM_DEVICEID   0x00000004 //devcieid ��Ч,ע:��ֵ��Ч,������������־λ

//��ϲ�ѯ�豸��ͨ��
//call
//type:TYPE_MEET_INTERFACE_DEVICEINFO
//method: METHOD_MEET_INTERFACE_MAKEVIDEO--Type_MeetVideoDetailInfo����
typedef struct
{
	Type_HeaderInfo hdr;
	int32u			queryflag;//��ѯ��־,����ָ����ѯ����
	int32u			devcietype;//��mask��Ͽ��Բ�ѯ���ϵ��豸 devcieid & mask == devcietype �μ�deviceid_suffix.h
	int32u			mask;//0xff00000
	int32u		    netstate;//0=���� 1=����
	int32u			devcieid;//��ѯָ���豸,Ϊ0��ʾ����
}Type_DeviceQueryStream, *pType_DeviceQueryStream;

typedef struct
{
	int32u devceid;

	//����豸ID�ǲλ���Աϯλ,����������Ч
	int32u memberid;
	char   name[DEFAULT_NAME_MAXLEN];
}Item_DeviceResPlay, *pItem_DeviceResPlay;

//��ѯ��ǰ�ɼ��벥�ŵ��豸��
//call
//type:TYPE_MEET_INTERFACE_DEVICEINFO
//method: METHOD_MEET_INTERFACE_RESINFO
typedef struct
{
	Type_HeaderInfo hdr;

	int32u			devnum;
	
}Type_DeviceResPlay, *pType_DeviceResPlay;

//��ѯָ���豸��ĳ������
//property id
#define MEETDEVICE_PROPERTY_NAME				1 //�豸���� query/set(string)
#define MEETDEVICE_PROPERTY_IPADDR				2 //ip��ַ query paramterval(ip��ַ����)(string)
#define MEETDEVICE_PROPERTY_PORT				3 //����˿� query paramterval(ip��ַ����)(propertyval int32u)
#define MEETDEVICE_PROPERTY_NETSTATUS			4 //����״̬ query(propertyval int32u)
#define MEETDEVICE_PROPERTY_PLAYSTATUS			5 //����״̬ paramterval(res��ַ����)(propertyval2(triggerid), propertyval3(val),propertyval4(val2),propertyval4(createdeviceid)��Ч) query
#define MEETDEVICE_PROPERTY_FACESTATUS			6 //����״̬ query(propertyval int32u) (propertyval2(meetingid), propertyval3(memberid)
#define MEETDEVICE_PROPERTY_MEMBERID			7 //��ǰ�λ���ԱID query(propertyval int32u) (propertyval2(meetingid), propertyval3(facestatus)
#define MEETDEVICE_PROPERTY_MEETINGID			8 //��ǰ����ID query(propertyval int32u) (propertyval2(memberid), propertyval3(facestatus)
#define MEETDEVICE_PROPERTY_TRIGGERID			9 //��ǰ����ִ�еĴ�����ID query paramterval(res��ַ����) 
#define MEETDEVICE_PROPERTY_STREAMNAME			10 //�豸����ͨ������ query paramterval=��ѯ����ͨ���� (string)
#define MEETDEVICE_PROPERTY_RESOPERTORID		11 //��ȡ��Դ�������豸ID query paramterval(res��ַ����)
#define MEETDEVICE_PROPERTY_TYPEAVAILABLE		12 //ĳ���豸�Ƿ���� query deviceid(�豸���ID,eg:DEVICE_MEET_SERVICE) propertyval=1���ã�=0������
#define MEETDEVICE_PROPERTY_VERSION				13 //��ȡָ���豸�İ汾 query propertyval���ذ汾��propertyval(hard):propertyval2(soft) 
#define MEETDEVICE_PROPERTY_DEVICEFLAG			14 //��ȡָ���豸������ query/set (propertyval int32u)  �����豸���� �μ�owbash.h MEETDEVICE_FLAG ����
#define MEETDEVICE_PROPERTY_FACESTATUSBYMEMBERID 15 //���λ���ԱID���ؽ���״̬(��Ҫ������λ) query(propertyval int32u) (propertyval2(meetingid), propertyval3(memberid)
#define MEETDEVICE_PROPERTY_NETSTATUSBYMEMBERID  16 //���λ���ԱID��������״̬(��Ҫ������λ) query(propertyval int32u)
#define MEETDEVICE_PROPERTY_MULTICAST			 17 //���ص�ָ������������·�Ƿ�֧���鲥 deviceid=��������ID query(propertyval int32u)
#define MEETDEVICE_PROPERTY_DEVICECHECK  		18 //ָ�����豸���߷������Ƿ��Ѿ�̽��ɹ� query(propertyval int32u) 1��ʾ�Ѿ�̽��ɹ�, 0��ʾδ̽�⵽


#define MEET_DEVICESTRING_MAXLEN 260
//type:TYPE_MEET_INTERFACE_DEVICEINFO
//method: queryproperty
typedef struct
{
	Type_HeaderInfo hdr;

	int32u propertyid;//����ID
	int32u deviceid;//������� Ϊ0��ʾ�����豸ID
	int32u paramterval;//�������

	int32u propertyval; //���� //0���У�1���Ž�Ŀ��2��������3�Խ�
	int32u propertyval2;//����
	int32u propertyval3;//����
	int32u propertyval4;//����
	int32u propertyval5;//����
}Type_MeetDeviceQueryProperty_int32u, *pType_MeetDeviceQueryProperty_int32u;

//type:TYPE_MEET_INTERFACE_DEVICEINFO
//method: queryproperty
typedef struct
{
	Type_HeaderInfo hdr;

	int32u propertyid;//����ID
	int32u deviceid;//������� Ϊ0��ʾ�����豸ID
	int32u paramterval;//�������
	char   propertytext[MEET_DEVICESTRING_MAXLEN];//�ַ���

}Type_MeetDeviceQueryProperty_string, *pType_MeetDeviceQueryProperty_string;

//�������״̬֪ͨ
//type:TYPE_MEET_INTERFACE_DEVICEMEETSTATUS
//method: qeuery/notify
typedef struct
{
	Type_HeaderInfo hdr;

	int32u  deviceid;
	int32u  memberid;
	int32u  oldfacestatus;//// �μ�MemState_MainFace ���� ��ֵֻ����notify֪ͨʱ����Ч
	int32u  facestatus; // �μ�MemState_MainFace ����
	int32u  meetingid;
}Type_MeetDeviceMeetStatus, *pType_MeetDeviceMeetStatus;

//�޸ı����Ļ������״̬
//type:TYPE_MEET_INTERFACE_DEVICEMEETSTATUS
//method: qeuery/modify
typedef struct
{
	Type_HeaderInfo hdr;

	int32u  facestatus; // �μ�MemState_MainFace ����
}Type_MeetModDeviceMeetStatus, *pType_MeetModDeviceMeetStatus;

//ˢ���豸�Ļ�����Ϣ\�㲥��ǰ����λ�����������Ϣ
//call
//type:TYPE_MEET_INTERFACE_DEVICEMEETSTATUS
//method: refresh/broadcast
typedef struct
{
	Type_HeaderInfo hdr;
}Type_DeviceRefreshInfo, *pType_DeviceRefreshInfo;

//modflag
#define DEVICE_MODIFYFLAG_NAME			0x00000001 //��ʾ�޸�����
#define DEVICE_MODIFYFLAG_IPADDR		0x00000002 //��ʾ�޸�ip��ַ
#define DEVICE_MODIFYFLAG_PORT			0x00000004 //��ʾ�޸Ķ˿�
#define DEVICE_MODIFYFLAG_LIFTRES		0x00000008 //��ʾ�޸���������
#define DEVICE_MODIFYFLAG_DEVICEFLAG    0x000000016 //��ʾ�޸Ļ����豸����
//�޸��豸��Ϣ
//call
//type:TYPE_MEET_INTERFACE_DEVICEINFO
//method: modifyinfo
typedef struct
{
	Type_HeaderInfo hdr;

	int32u modflag;//ָ����Ҫ�޸ĵı�־λ

	int32u devcieid;
	char   devname[DEVICE_NAME_MAXLEN];
	SubItem_DeviceIpAddrInfo ipinfo[MAX_DEVICEIPADDR_NUM];
	int8u  liftgroupres[2];//0��������ID 1������Ͳ��ID
	int8u   deviceflag;//�����豸���� �μ�owbash.h MEETDEVICE_FLAG ����
}Type_DeviceModInfo, *pType_DeviceModInfo;

//ɾ���豸
//call
//type:TYPE_MEET_INTERFACE_DEVICEINFO
//method: del
typedef struct
{
	Type_HeaderInfo hdr;

	int32u			devnum;
	//int32u devid[devnum];
}Type_DeviceDel, *pType_DeviceDel;

//�����豸
//call
//type:TYPE_MEET_INTERFACE_DEVICEINFO
//method: update
typedef struct
{
	Type_HeaderInfo hdr;
	int32u		mediaid;//�Ѿ��ϴ���ɵ������ļ�ID
}Type_DoDeviceUpdate, *pType_DoDeviceUpdate;

//��������
//callback
//type:TYPE_MEET_INTERFACE_DEVICEOPER
//method: zoom
typedef struct
{
	Type_HeaderInfo hdr;

	int   zoomper;

	int   avalableres;//��Ч��Դ����,Ϊ0��ʾȫ��
	int8u res[32];
}Type_MeetZoomResWin, *pType_MeetZoomResWin;

//��������
//call
//type:TYPE_MEET_INTERFACE_DEVICEOPER
//method: zoom
typedef struct
{
	Type_HeaderInfo hdr;

	int   zoomper;

	int   avalableres;//��Ч��Դ����,Ϊ0��ʾȫ��
	int8u res[32];

	int   devnum;
	//int32u deviceid[devnum];
}Type_MeetDoZoomResWin, *pType_MeetDoZoomResWin;

//�ӵ��λ���Ա������ǩ��������֪ͨ
//callback
//type:TYPE_MEET_INTERFACE_DEVICEOPER
//method: METHOD_MEET_INTERFACE_BROADCAST
typedef struct
{
	Type_HeaderInfo hdr;

	int32u deviceid;//�㲥���豸ID
	int32u memberid;//�㲥�߲λ���ԱID

	int32u membersize; //�����й㲥ʱ�Ĳλ���Ա����
	int32u membersignsize;////�����й㲥ʱ�Ĳλ���Ա�Ѿ�ǩ��������
}Type_MeetMemberCastInfo, *pType_MeetMemberCastInfo;

//�������绽���豸
//call
//type:TYPE_MEET_INTERFACE_DEVICEOPER
//method: reboot
typedef struct
{
	Type_HeaderInfo hdr;

	int   devnum;
	//int32u deviceid[devnum];
}Type_MeetDoNetReboot, *pType_MeetDoNetReboot;

//������鹦�ܽ���
//callback
//type:TYPE_MEET_INTERFACE_DEVICEOPER
//method: enter
typedef struct
{
	Type_HeaderInfo hdr;
}Type_MeetEnterMeet, *pType_MeetEnterMeet;

//������鹦�ܽ���
//call
//type:TYPE_MEET_INTERFACE_DEVICEOPER
//method: enter
typedef struct
{
	Type_HeaderInfo hdr;

	int   devnum;
	//int32u deviceid[devnum];
}Type_MeetDoEnterMeet, *pType_MeetDoEnterMeet;

//�ص�������
//callback
//type:TYPE_MEET_INTERFACE_DEVICEOPER
//method: back
typedef struct
{
	Type_HeaderInfo hdr;
}Type_MeetToHomePage, *pType_MeetToHomePage;

//�ص�������
//call
//type:TYPE_MEET_INTERFACE_DEVICEOPER
//method: back
typedef struct
{
	Type_HeaderInfo hdr;

	int   devnum;
	//int32u deviceid[devnum];
}Type_MeetDoToHomePage, *pType_MeetDoToHomePage;

//���������Ϊ����Ա
//call
//type:TYPE_MEET_INTERFACE_DEVICEOPER
//method: METHOD_MEET_INTERFACE_REQUESTTOMANAGE
typedef struct
{
	Type_HeaderInfo hdr;

	//ָ���յ���������豸ID
	int     devnum;
	//int32u deviceid[devnum];

}Type_MeetRequestManage, *pType_MeetRequestManage;

//�յ������Ϊ����Ա����
//callback
//type:TYPE_MEET_INTERFACE_DEVICEOPER
//method: METHOD_MEET_INTERFACE_REQUESTTOMANAGE
typedef struct
{
	Type_HeaderInfo hdr;

	int32u  deviceid;       //����������豸ID
	int32u  memberid;       //�����������ԱID

}Type_MeetRequestManageNotify, *pType_MeetRequestManageNotify;

//�ظ������Ϊ����Ա����
//call
//type:TYPE_MEET_INTERFACE_DEVICEOPER
//method: METHOD_MEET_INTERFACE_RESPONSETOMANAGE
typedef struct
{
	Type_HeaderInfo hdr;

	int32u  returncode;		//1=ͬ��,0=��ͬ��

	//ָ���յ���������豸ID
	int     devnum;
	//int32u deviceid[devnum];

}Type_MeetResponseRequestManage, *pType_MeetResponseRequestManage;

//�յ������Ϊ����Ա�ظ�
//callback
//type:TYPE_MEET_INTERFACE_DEVICEOPER
//method: METHOD_MEET_INTERFACE_RESPONSETOMANAGE
typedef struct
{
	Type_HeaderInfo hdr;

	int32u  deviceid;       //����������豸ID
	int32u  memberid;       //�����������ԱID
	int32u  returncode;		//1=ͬ��,0=��ͬ��
}Type_MeetRequestManageResponse, *pType_MeetRequestManageResponse;

//��������λ���ԱȨ������
//call
//type:TYPE_MEET_INTERFACE_DEVICEOPER
//method: METHOD_MEET_INTERFACE_REQUESTPRIVELIGE
typedef struct
{
	Type_HeaderInfo hdr;

	int32u  privilege;//�����Ȩ�� �����ļ� �λ���Ȩ�� �궨��

	//ָ���յ���������豸ID
	int     devnum;
	//int32u deviceid[devnum];

}Type_MeetRequestPrivilege, *pType_MeetRequestPrivilege;

//�յ�����λ���ԱȨ������
//callback
//type:TYPE_MEET_INTERFACE_DEVICEOPER
//method: METHOD_MEET_INTERFACE_REQUESTPRIVELIGE
typedef struct
{
	Type_HeaderInfo hdr;

	int32u  deviceid;       //����������豸ID
	int32u  memberid;       //�����������ԱID
	int32u  privilege;		//�����Ȩ�� �����ļ� �λ���Ȩ�� �궨��
}Type_MeetRequestPrivilegeNotify, *pType_MeetRequestPrivilegeNotify;

//�ظ��λ���ԱȨ������
//call
//type:TYPE_MEET_INTERFACE_DEVICEOPER
//method: METHOD_MEET_INTERFACE_RESPONSEPRIVELIGE
typedef struct
{
	Type_HeaderInfo hdr;

	int32u  privilege;		//�ظ������Ȩ�� �����ļ� �λ���Ȩ�� �궨��

	//ָ���յ���������豸ID
	int     devnum;
	//int32u deviceid[devnum];

}Type_MeetResponseRequestPrivilege, *pType_MeetResponseRequestPrivilege;

//�յ��λ���ԱȨ������ظ�
//callback
//type:TYPE_MEET_INTERFACE_DEVICEOPER
//method: METHOD_MEET_INTERFACE_RESPONSEPRIVELIGE
typedef struct
{
	Type_HeaderInfo hdr;

	int32u  deviceid;       //����������豸ID
	int32u  memberid;       //�����������ԱID
	int32u  privilege;		//�ظ������Ȩ�� �����ļ� �λ���Ȩ�� �궨��
}Type_MeetRequestPrivilegeResponse, *pType_MeetRequestPrivilegeResponse;

//texttype
#define TEXTBRODCAST_TYPE_COMMONTEXT  0 //��ͨ�ı���Ϣ
#define TEXTBRODCAST_TYPE_MEETINGTEXT 1 //���������ı���Ϣ
#define TEXTBRODCAST_TYPE_MEMBERTEXT  2 //�λ����ı���Ϣ
#define TEXTBRODCAST_TYPE_IATTEXT     3 //�����Ҫ�ı���Ϣ
#define TEXTBRODCAST_TYPE_SUMARYTEXT  4 //�����Ҫ�ı���Ϣ
#define TEXTBRODCAST_TYPE_CLOSTSUMARY 5 //�رջ����Ҫ�ı���Ϣ

//�����ı��㲥
//call
//type:TYPE_MEET_INTERFACE_DEVICEOPER
//method: METHOD_MEET_INTERFACE_TEXTMSG
typedef struct
{
	Type_HeaderInfo hdr;

	//ָ���յ���������豸ID
	int     devnum;
	//int32u deviceid[devnum];

	int32u  texttype;//�ı�����
	char*   ptextmsg;//�ı���Ϣ
}Type_MeetDoTextBrodcast, *pType_MeetDoTextBrodcast;

//�յ��ı��㲥
//callback
//type:TYPE_MEET_INTERFACE_DEVICEOPER
//method: METHOD_MEET_INTERFACE_TEXTMSG
typedef struct
{
	Type_HeaderInfo hdr;

	int32u  texttype;//�ı�����
	char*   ptext;//�ı���Ϣ
}Type_MeetTextBrodcast, *pType_MeetTextBrodcast;

//Զ�������豸����
//call
//type:TYPE_MEET_INTERFACE_DEVICEOPER
//method: METHOD_MEET_INTERFACE_REMOTESET
typedef struct
{
	Type_HeaderInfo hdr;

	//ָ���յ���������豸ID
	int     devnum;
	//int32u deviceid[devnum];

	int    jsontextlen;//Ҫ���õ����ò��� ,�μ��·���jsonЭ�鶨��
	
	/*
	��ʽ��
	{
	"restart":0,//0=��������� 1=�������,Ĭ���������
	"item":[//�����ļ��޸ĵĲ��� ,�����ö��ѡ��
		{
		"section":"debug",//ini�ڵ�
		"key":"console",//����
		"value":"1"//��ֵ
		}
		]
	}

	eg:���õ���ѡ��,���������
	{
	"restart":1,
	"item":[
	{"section":"debug","key":"console","value":"1"}
	]
	}
	*/
}Type_MeetDoRemoteSet, *pType_MeetDoRemoteSet;

//�յ�Զ�������豸����
//callback
//type:TYPE_MEET_INTERFACE_DEVICEOPER
//method: METHOD_MEET_INTERFACE_REMOTESET
typedef struct
{
	Type_HeaderInfo hdr;

	int32u  deviceid;       //����������豸ID
	char*   ptext;//�ı���Ϣ
}Type_MeetRemoteSetNotify, *pType_MeetRemoteSetNotify;

//����������  ��ѯ��������

//property id
#define MEETCONTEXT_PROPERTY_ROLE				1 //��ǰ����Ľ�ɫ set/query(int32u)
#define MEETCONTEXT_PROPERTY_CURADMINID			2 //��ǰ�����½�Ĺ���ԱID query(int32u)
#define MEETCONTEXT_PROPERTY_CURADMINNAME		3 //��ǰ�����½�Ĺ���Ա�û��� query(string)
#define MEETCONTEXT_PROPERTY_CURADMINPASSWORD	4 //��ǰ�����½�Ĺ���Ա���� md5 query(string)
#define MEETCONTEXT_PROPERTY_HASTABLECARD		5 //��ǰ�����Ƿ���˫�� set/query(int32u)
#define MEETCONTEXT_PROPERTY_CURMEETINGID		6 //��ǰ����Ļ���ID set/query(int32u)
#define MEETCONTEXT_PROPERTY_SELFID				7 //�����豸ID query(int32u)
#define MEETCONTEXT_PROPERTY_CURROOMID			8 //��ǰ����Ļ᳡ID query(int32u)
#define MEETCONTEXT_PROPERTY_SELFMEMBERID		9 //��ǰ����İ󶨵Ĳλ���ԱID query(int32u)
#define MEETCONTEXT_PROPERTY_MEETDBDEVICEID		10 //��ǰ�������ݿ��豸ID query(int32u)
#define MEETCONTEXT_PROPERTY_SCREENSTREAMINDEX	11 //������Ļ��ͨ���� query(int32u)
#define MEETCONTEXT_PROPERTY_AVAILABLEMEDIASERER	12 //ý��������Ƿ���� query propertyval=���ؿ��õĸ���(int32u)
#define MEETCONTEXT_PROPERTY_AVAILABLESTREAMSERVER	13 //���������Ƿ���� query=���ؿ��õĸ���(int32u)
#define MEETCONTEXT_PROPERTY_UTCMICROSECONDS	14 //���ص�ǰϵͳ΢��UTCʱ�� query(int64u)
#define MEETCONTEXT_PROPERTY_LOCALCICROSECONDS	15 //���ص�ǰϵͳ΢��ʱ�� query(int64u)
#define MEETCONTEXT_PROPERTY_HARDVERSION     	16 //����Ӳ���汾�� query(int32u)
#define MEETCONTEXT_PROPERTY_SOFTVERSION     	17 //��������汾�� query(int32u)
#define MEETCONTEXT_PROPERTY_ENTERPRISEID     	18 //�����豸������ҵID query(int32u)
#define MEETCONTEXT_PROPERTY_ENTERPRISENAME    	19 //�����豸������ҵ���� query(string)
#define MEETCONTEXT_PROPERTY_SESELLMARK			20 //�����豸���۱�ʶ query(string)
#define MEETCONTEXT_PROPERTY_EXPIRATIONTIME		21 //�����������Чʱ�� query(int64u) UTCʱ�� ��λ���� Ϊ0xffffffffffffff��ʾ������Ч
#define MEETCONTEXT_PROPERTY_AREAENTERPRISEID	22 //�����������ҵID query(int32u)
#define MEETCONTEXT_PROPERTY_REGISTERUSERDEF32	23 //����ע��ʱ���û��Զ���ֵ query(int32u) -->��ֵ ��ʱ������ҳ��������������
#define MEETCONTEXT_PROPERTY_MAXONLINENUM		24 //�豸����������� query(int32u)
#define MEETCONTEXT_PROPERTY_MINCAPTURETIME		25 //���ɼ������Сֵ ��λ���� query(int32u)
#define MEETCONTEXT_PROPERTY_ENCODEMODE  		26 //���ɼ��ı���ģʽ query(int32u)
#define MEETCONTEXT_PROPERTY_STREAMSAVE  		27 //�������־ set/query(int32u) 1��ʾ����, 0��ʾ������
#define MEETCONTEXT_PROPERTY_MEDIACHECK  		28 //ָ�����豸���߷������Ƿ��Ѿ�̽��ɹ� query(propertyval int32u) 1��ʾ�Ѿ�̽��ɹ�, 0��ʾδ̽�⵽
#define MEETCONTEXT_PROPERTY_MEETCACHEDIR  		29 //��ȡ�������߻����Ŀ¼ query(string)
#define MEETCONTEXT_PROPERTY_LOGONMODE  		30 //��ȡ��½ģʽ�����õ�½ʱ�ӿڻᱣ�� set/query(string)
#define MEETCONTEXT_PROPERTY_HASONLIETRIGGER	31 //�Ƿ�������ߴ����¼� query(int32u) ���ش�����ID 0��ʾû��

//type: TYPE_MEET_INTERFACE_MEETCONTEXT
//method: queryproperty/setproperty
typedef struct
{
	Type_HeaderInfo hdr;

	int32u propertyid;//����ID
	int32u propertyval;//ֵ 
}Type_MeetContextInfo_int32u, *pType_MeetContextInfo_int32u;

//type: TYPE_MEET_INTERFACE_MEETCONTEXT
//method: queryproperty/setproperty
typedef struct
{
	Type_HeaderInfo hdr;

	int32u propertyid;//����ID
	int64u propertyval;//ֵ 
}Type_MeetContextInfo_int64u, *pType_MeetContextInfo_int64u;

//���������� ��ѯ�ַ���
#define MEET_CONTEXTSTRING_MAXLEN 260
//type: TYPE_MEET_INTERFACE_MEETCONTEXT
//method: query /set
typedef struct
{
	Type_HeaderInfo hdr;

	int32u propertyid;//����ID
	char   propertytext[MEET_CONTEXTSTRING_MAXLEN];//�ַ���
}Type_MeetContextInfo_string, *pType_MeetContextInfo_string;

//������ҳ
#define MEET_URL_ADDR_MAXLEN 260
typedef struct
{
	int32u  id;	//���ڱ�ʶ�޸�ɾ������
	char    name[DEFAULT_DESCRIBE_LENG];//��ַ�����ַ���
	char    addr[MEET_URL_ADDR_MAXLEN];//��ַ���ݣ��ַ���
}Item_MeetUrlInfo, *pItem_MeetUrlInfo;

//type: TYPE_MEET_INTERFACE_DEFAULTURL
//method: modify,query
typedef struct
{
	Type_HeaderInfo hdr; //��Ҫ��Hdr.meetingid���и�ֵ,0=��ʾ�޸�Ĭ�ϵ���ַ
	int urlnum;
	//Item_MeetUrlInfo [urlnum]
}Type_meetUrl, *pType_meetUrl;

//ɾ��������ַ
//type: TYPE_MEET_INTERFACE_DEFAULTURL
//method: delete
typedef struct
{
	Type_HeaderInfo hdr; //��Ҫ��Hdr.meetingid���и�ֵ,0=��ʾɾ��Ĭ�ϵ���ַ
	int urlnum;
	//int32u [urlnum]
}Type_deletemeetUrl, *pType_deletemeetUrl;

//������ҳ���
//type: TYPE_MEET_INTERFACE_DEFAULTURL
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;
	int32u opermethod;//����֪ͨ����Ϊopermethod����������
	int32u id;//���ָ����ID�ձ�ʾ�Ƕ��ض�ID�Ĳ���,Ϊ0��ʾȫ��

}Type_MeetUrlNotifyInfo, *pType_MeetUrlNotifyInfo;

//�������
//type: TYPE_MEET_INTERFACE_MEETAGENDA
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;
	int32u opermethod;//����֪ͨ����Ϊopermethod����������
	int32u id;//���ָ����ID�ձ�ʾ�Ƕ��ض�ID�Ĳ���,Ϊ0��ʾȫ��

}Type_MeetAgendaInfo, *pType_MeetAgendaInfo;

//�������
//agendatype
#define MEET_AGENDA_TYPE_TEXT  0 //�ı� val=�ı�����
#define MEET_AGENDA_TYPE_FILE  1 //�ļ� val=ý��ID
#define MEET_AGENDA_TYPE_TIME  2 //ʱ����ʽ val=Item_AgendaTimeInfo����
#define MEET_AGENDA_TYPE_NOSET 0xff000000 //��ѯ���ʱ��ָ���ñ�־��ʾ��ѯ��ǰ����ʹ�õ����

//ʱ����ʽ�������
#define MEETAGENDA_STATUS_IDLE		0 //δ����
#define MEETAGENDA_STATUS_RUNNING   1 //������
#define MEETAGENDA_STATUS_END		2 //�ѽ���

#define MEETAGENDA_DESCTEXT_LENG    320
typedef struct
{
	int32u  agendaid;  //���ID
	int32u  status;  //���״̬
	int32u  dirid;  //��Ŀ¼ID
	int64u  startutctime;//��λ��
	int64u  endutctime;  //��λ��
	char    desctext[MEETAGENDA_DESCTEXT_LENG]; //��������
}Item_AgendaTimeInfo, *pItem_AgendaTimeInfo;

//type: TYPE_MEET_INTERFACE_MEETAGENDA
//method: (MEET_AGENDA_TYPE_TEXT|MEET_AGENDA_TYPE_FILE:modify,query)��MEET_AGENDA_TYPE_TIME:modify,query,add,del,set��
typedef struct
{
	Type_HeaderInfo hdr;
	
	int32u	 agendatype;//�������
	int32u   val;// 
}Type_meetAgenda, *pType_meetAgenda;

//���鹫��
//TYPE_MEET_INTERFACE_MEETBULLET
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;
	int32u opermethod;//����֪ͨ����Ϊopermethod����������
	int32u id;//���ָ����ID�ձ�ʾ�Ƕ��ض�ID�Ĳ���,Ϊ0��ʾȫ��

}Type_MeetBulletInfo, *pType_MeetBulletInfo;

//TYPE_MEET_INTERFACE_MEETBULLET
//call
//method: request,update
typedef struct
{
	Type_HeaderInfo hdr;

	int textlen;
	//char text[textlen];
}Type_BigBulletDetailInfo, *pType_BigBulletDetailInfo;

//���鹫��
#define DEFAULT_BULLETCONTENT_LENG 320
typedef struct
{
	int32u		bulletid;
	char	    title[DEFAULT_DESCRIBE_LENG]; //����
	char	    content[DEFAULT_BULLETCONTENT_LENG]; //���� 
	int32u		type;//���
	int32u		starttime; //��ʼʱ��
	int32u		timeouts;  //��ʱֵ
}Item_BulletDetailInfo, *pItem_BulletDetailInfo;

//TYPE_MEET_INTERFACE_MEETBULLET
//call
//method: add,del,modify,query
typedef struct
{
	Type_HeaderInfo hdr;

	int num;
}Type_BulletDetailInfo, *pType_BulletDetailInfo;

//���鹫�淢��
//call callback
//TYPE_MEET_INTERFACE_MEETBULLET
//method: publish
typedef struct
{
	Type_HeaderInfo hdr;
	Item_BulletDetailInfo bullet;

	//�����������������벻��Ҫ��д��֪ͨ����Ч
	int32u operdeviceid;//�����豸
	int32u opermember;//������ԱID

	int devnum; //�����Ǹ��豸
	//int32u deviceid[];
}Type_MeetPublishBulletInfo, *pType_MeetPublishBulletInfo;

//ֹͣ���鹫��
//TYPE_MEET_INTERFACE_MEETBULLET
//call
//method: stop
typedef struct
{
	Type_HeaderInfo hdr;

	int32u  bulletid;//ֹͣ�Ļ��鹫��ID
	int     devnum; //�豸ID������Ϊ0��ʾ���е��豸
	//int32u pdevid[];//�豸ID
}Type_StopBullet, *pType_StopBullet;

//ֹͣ���鹫��
//TYPE_MEET_INTERFACE_MEETBULLET
//callback
//method: stop
typedef struct
{
	Type_HeaderInfo hdr;

	int32u  bulletid;//ֹͣ�Ļ��鹫��ID
}Type_StopBulletMsg, *pType_StopBulletMsg;

//����Ա
//callback
//TYPE_MEET_INTERFACE_ADMIN
//method:notify
typedef struct
{
	Type_HeaderInfo hdr;

	int32u opermethod;//����֪ͨ����Ϊopermethod����������
	int32u id;//���ָ����ID�ձ�ʾ�Ƕ��ض�ID�Ĳ���,Ϊ0��ʾȫ��
}Type_meetAdmin, *pType_meetAdmin;

typedef struct
{
	int32u adminid;
	char   adminname[DEFAULT_NAME_MAXLEN];
	char   pw[DEFAULT_PASSWORD_LENG];//���� md5 ascill 32�ֽ�
	char   comment[DEFAULT_DESCRIBE_LENG];
	char   phone[DEFAULT_SHORT_DESCRIBE_LENG];
	char   email[DEFAULT_SHORT_DESCRIBE_LENG];
}Item_AdminDetailInfo, *pItem_AdminDetailInfo;

//TYPE_MEET_INTERFACE_ADMIN
//method: add\del\modify\query
typedef struct
{
	Type_HeaderInfo hdr;
	int32u			adminnum;
}TypeAdminDetailInfo, *pTypeAdminDetailInfo;

//����Ա��½
#define MEET_MD5_ASCILL_MAXLEN 32

//TYPE_MEET_INTERFACE_ADMIN
//method: set
typedef struct
{
	Type_HeaderInfo hdr;

	char			adminname[DEFAULT_NAME_MAXLEN];//�û���
	char			adminoldpwd[MEET_MD5_ASCILL_MAXLEN];//�û�����(ascill/md5ascill)
	char			adminnewpwd[MEET_MD5_ASCILL_MAXLEN];//�û�����(ascill/md5ascill)
}Type_AdminModifyPwd, *pType_AdminModifyPwd;

//Type_AdminLogon -- > flag
#define ADMINLOGON_FLAG_ISASCILL 0x01  //�Ƿ�Ϊ���������ַ� Ĭ��������

//Type_AdminLogon -- > logonmode
#define ADMINLOGON_MODE_ADMIN    0  //����ģʽ
#define ADMINLOGON_MODE_COMMON   1  //������Աģʽ
#define ADMINLOGON_MODE_LOCAL    2  //����ģʽ
//TYPE_MEET_INTERFACE_ADMIN
//method: logon
typedef struct
{
	Type_HeaderInfo hdr;

	int8u			flag;//�Ƿ��Ѿ�md5���� ADMINLOGON_FLAG_ISASCILL
	int8u			logonmode;//ADMINLOGON_MODE_COMMON
	int8u			fill[2];//
	char			adminname[DEFAULT_NAME_MAXLEN];//�û��� ������Ա�ֻ���
	char			adminpwd[MEET_MD5_ASCILL_MAXLEN];//�û�����(ascill/md5ascill)
}Type_AdminLogon, *pType_AdminLogon;

//��½����
#define ADMINLOGON_ERR_NONE 0 //��½�ɹ�
#define ADMINLOGON_ERR_PSW  1 //�������
#define ADMINLOGON_ERR_EXCPT_SV  2 //�������쳣
#define ADMINLOGON_ERR_EXCPT_DB  3 //���ݿ��쳣

//TYPE_MEET_INTERFACE_ADMIN
//method: logon
typedef struct
{
	Type_HeaderInfo hdr;
	int32u			err;//
	int32u			sessionid;//�ỰID
	int32u			adminid;//�ỰID
	char			adminname[DEFAULT_NAME_MAXLEN];//�ỰID
}Type_AdminLogonStatus, *pType_AdminLogonStatus;

//�������Ա���ƵĻ᳡
//TYPE_MEET_INTERFACE_MANAGEROOM
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;
	int32u opermethod;//����֪ͨ����Ϊopermethod����������
	int32u id;//���ָ����ID�ձ�ʾ�Ƕ��ض�ID�Ĳ���,Ϊ0��ʾȫ��

}Type_MeetManageRoom, *pType_MeetManageRoom;

//�������Ա���ƵĻ᳡
typedef struct
{
	int32u		roomid; //�᳡ID
}Item_MeetManagerRoomDetailInfo, *pItem_MeetManagerRoomDetailInfo;

//type:TYPE_MEET_INTERFACE_MANAGEROOM
//method: query/save
typedef struct
{
	Type_HeaderInfo hdr;

	int32u   mgrid;//����ԱID
	int32u	 num; //
}Type_MeetManagerRoomDetailInfo, *pType_MeetManagerRoomDetailInfo;

//fontcolor index
//white=0 black red green blue yellow darkred darkgreen darkblue darkyellow transparent gray darkgray lightgray magenta darkmagenta cyan darkcyan color0 color1
//�ն˿���
//oper
#define DEVICECONTORL_SHUTDOWN 1 //�ػ�
#define DEVICECONTORL_REBOOT  2 //����
#define DEVICECONTORL_PROGRAMRESTART  3 //�������
#define DEVICECONTORL_LIFTUP  4 //�� operval1��Ч �ο�LIFT_FLAG_MACHICE
#define DEVICECONTORL_LIFTDOWN  5 //�� operval1��Ч �ο�LIFT_FLAG_MACHICE
#define DEVICECONTORL_LIFTSTOP  6 //ֹͣ�������� operval1��Ч �ο�LIFT_FLAG_MACHICE
#define DEVICECONTORL_MODIFYLOGO  7 //����LOGO operval1��Ч ָý��ID
#define DEVICECONTORL_MODIFYMAINBG  8 //���������� operval1��Ч ָý��ID
#define DEVICECONTORL_MODIFYPROJECTBG  9 //����ͶӰ���� operval1��Ч ָý��ID
#define DEVICECONTORL_MODIFYSUBBG  10 //�����ӽ��� operval1��Ч ָý��ID
#define DEVICECONTORL_MODIFYFONTCOLOR  11 //����������ɫ operval1��Ч ָ��ɫ��� �μ�fontcolor index

//callback
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;
	int32u			oper;//
	int32u			operval1;//������Ӧ�Ĳ��� ������������ý��ID
	int32u			operval2;//������Ӧ�Ĳ���
}Type_DeviceControl, *pDeviceControl;

//operval1 ������������־
#define LIFT_FLAG_MACHICE 0x00000001 //ѡ�����������
#define LIFT_FLAG_MIC     0x00000002 //ѡ��������Ͳ���

//TYPE_MEET_INTERFACE_DEVICEINFO
//method: control
typedef struct
{
	Type_HeaderInfo hdr;
	int32u			oper;//
	int32u			operval1;//������Ӧ�Ĳ��� ������������ý��ID
	int32u			operval2;//������Ӧ�Ĳ���
	int32u			devnum; //���Ƶ��豸�� Ϊ0��ǰ�����ʾ����
}Type_DeviceOperControl, *pType_DeviceOperControl;

//����
#define MEET_PATH_MAXLEN 1024
//method: add
typedef struct
{
	Type_HeaderInfo hdr;

	int32u mediaid;					//����ý��id
	int	   newfile;				    //�����Ƿ���������,0 ������ͬ���ļ�,��������
	void*  puser;					//�û�ָ��
	int	   onlyfinish;				//1��ʾֻ��Ҫ������֪ͨ
	char   pathname[MEET_PATH_MAXLEN];//����ý��ȫ·������
	char   userstr[DEFAULT_DESCRIBE_LENG];//�û�������Զ����ִ�
}Type_DownloadStart, *pType_DownloadStart;

//���߱��ػ��� ע:��Ҫ���顢�����ҡ�����Ŀ¼������Ŀ¼�ļ���׼���òŻ�ɹ�ִ��
//method: CACHE
typedef struct
{
	Type_HeaderInfo hdr;

	int32u dirid;					//�����Ŀ¼ID
	int32u mediaid;					//����ý��id
	int	   newfile;				    //�����Ƿ���������,0 ������ͬ���ļ�,��������
	void*  puser;					//�û�ָ��
	int	   onlyfinish;				//1��ʾֻ��Ҫ������֪ͨ
	char   userstr[DEFAULT_DESCRIBE_LENG];//�û�������Զ����ִ�
}Type_DownloadCache, *pType_DownloadCache;

//ɾ��һ������
//method: del
typedef struct
{
	Type_HeaderInfo hdr;

	int32u mediaid;	//����ý��id
}Type_DownloadDel, *pType_DownloadDel;

//�������
//method: clear
typedef struct
{
	Type_HeaderInfo hdr;
}Type_DownloadClear, *pType_DownloadClear;

//���� state
#define STATE_MEDIA_DOWNLOAD_IDLE	  0 
#define STATE_MEDIA_DOWNLOAD_WORKING  1 
#define STATE_MEDIA_DOWNLOAD_FINISH   2
#define STATE_MEDIA_DOWNLOAD_ERROR    3
#define STATE_MEDIA_DOWNLOAD_EXIT     4
#define STATE_MEDIA_DOWNLOAD_DESTROY  5

//���� err  
#define ERROR_MEDIA_DOWNLOAD_OK			0 //����
#define ERROR_MEDIA_DOWNLOAD_PARAMETER  1 //��������
#define ERROR_MEDIA_DOWNLOAD_INITTHREAD 2 //�̴߳���ʧ��
#define ERROR_MEDIA_DOWNLOAD_USERSTOP	3 //����ֹͣ
#define ERROR_MEDIA_DOWNLOAD_NOMEDIA	4 //�����ڵ�ý�� 
#define ERROR_MEDIA_DOWNLOAD_OPEN		5 //���ļ�ʧ��
#define ERROR_MEDIA_DOWNLOAD_FILEOK		6 //�ļ�����,����Ҫ����
#define ERROR_MEDIA_DOWNLOAD_CREATE		7 //���� �ļ�ʧ��
#define ERROR_MEDIA_DOWNLOAD_MEMORY		8 //�ڴ����ʧ��
#define ERROR_MEDIA_DOWNLOAD_EXECINIT	9 //ƽ̨���ʼ������ʧ��
#define ERROR_MEDIA_DOWNLOAD_SETPOS		10 //��������λ��ʧ��

//���ؽ���֪ͨ
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;

	int	   progress;				//���ؽ���
	int	   nstate;				    //����״̬,�ο�state flag
	int	   err;						//����ֵ ERROR_MEDIA_DOWNLOAD_OK
	int32u mediaid;					//����ý��id
	void*  puser;					//�û�ָ��
	char   pathname[MEET_PATH_MAXLEN];//����ý��ȫ·������ (windows:mbcs linux:utf8)
	char   userstr[DEFAULT_DESCRIBE_LENG];//�û�������Զ����ִ�(ԭ�����ʽ����)
}Type_DownloadCb, *pType_DownloadCb;

//���Ž���֪ͨ
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;

	int32u mediaId;						//ý��ID
	int8u  status;						//0=�����У�1=��ͣ��2=ֹͣ,3=�ָ�
	int8u  per;							//��ǰλ�ã��ٷֱ�
	int8u  resId;						//������ԴID
	int8u  _fill[1];
	int32u sec;							//��ǰ������������status>0ʱ��Ϊ�ļ�ID��
}Type_PlayPosCb, *pType_PlayPosCb;

//�ϴ�һ���ļ�
#define MEET_PATHNAME_MAXLEN 1024

//status
#define UPLOADMEDIA_FLAG_IDLE		0 //�ȴ��ϴ�
#define UPLOADMEDIA_FLAG_UPLOADING  1 //�ϴ���
#define UPLOADMEDIA_FLAG_STOP	    2 //ֹͣ
#define UPLOADMEDIA_FLAG_HADEND		3 //�����ϴ�
#define UPLOADMEDIA_FLAG_NOSERVER	4 //û�ҵ����õķ�����
#define UPLOADMEDIA_FLAG_ISBEING	5 //�Ѿ�����
//�ϴ�����֪ͨ
//callback
//type:TYPE_MEET_INTERFACE_UPLOAD
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;

	int32u mediaId;						//ý��ID
	int8u  status;					
	int8u  per;							//��ǰλ�ã��ٷֱ�
	int32u uploadflag;
	int32u userval;//
	char   pathname[MEET_PATHNAME_MAXLEN];//ȫ·����
	char   userstr[DEFAULT_DESCRIBE_LENG];//�û�������Զ����ִ�(ԭ�����ʽ����)
}TypeUploadPosCb, *pTypeUploadPosCb;

//uploadflag
#define MEET_UPLOADFLAG_ONLYENDCALLBACK 0x00000001 //ֻ�ܵ��ϴ������Żص�����

//call
//ϵͳ����ʹ�õ�Ŀ¼ID,�����ر���;
#define MEET_UPDATE_SYSTEMDIRID_CHANGELOGO			0xffffffff  //����LOGO
#define MEET_UPDATE_SYSTEMDIRID_CHANGEMAINBG		0xfffffffe  //����������
#define MEET_UPDATE_SYSTEMDIRID_CHANGEPROJECTIVEBG	0xfffffffd  //����ͶӰ������
#define MEET_UPDATE_SYSTEMDIRID_CHANGELECTURE   	0xfffffffc  //���������˽���
#define MEET_UPDATE_SYSTEMDIRID_CHANGEAGENDA    	0xfffffffb  //�������
#define MEET_UPDATE_SYSTEMDIRID_CHANGETOPIC      	0xfffffffa  //�������ļ�
#define MEET_UPDATE_SYSTEMDIRID_END					0xfffffff0 

//�ϴ��ļ���������
//type:TYPE_MEET_INTERFACE_UPLOAD
//method: add
typedef struct
{
	Type_HeaderInfo hdr;

	int32u uploadflag;//�ϴ���־
	int32u dirid;//�ϴ���Ŀ¼ID

	int32u attrib;//�ļ����� �μ� owbash.h MEET_FILEATTRIB_BACKGROUND ����
	char   newname[DEFAULT_FILENAME_LENG];//�ϴ����������
	char   pathname[MEET_PATHNAME_MAXLEN];//ȫ·����

	int32u userval;//
	int32u mediaid;//�ϴ�ʱ���ص�ý��ID --���Դ���ָ�����ļ�ID��ʾ�����ϴ�,���������޸��ĵ�����
	char   userstr[DEFAULT_DESCRIBE_LENG];//�û�������Զ����ִ�(ԭ�����ʽ����)
}Type_AddUploadFile, *pType_AddUploadFile;

//��ӱ����ļ�������Ŀ¼ ע:��Ҫ���顢�����Ҷ�׼���òŻ�ɹ�ִ��
//type:TYPE_MEET_INTERFACE_UPLOAD
//method: cache
typedef struct
{
	Type_HeaderInfo hdr;

	char   pathname[MEET_PATHNAME_MAXLEN];//�ļ�ȫ·����
	char   dirname[DEFAULT_FILENAME_LENG];//���浽�Ǹ�Ŀ¼,�����ڻ��Զ�����Ŀ¼
}Type_AddCacheFile, *pType_AddCacheFile;

//��ѯ�ϴ��ļ�
typedef struct
{
	int32u  mediaid;//ý��ID
	int32u  dirid;//�ϴ�����Ŀ¼ID

	int		errstatus;//������
	int32u  percent;//�ϴ��İٷֱ�
	int32u  userval;//

	char   newname[DEFAULT_FILENAME_LENG];//�ϴ����������
	char   pathname[MEET_PATHNAME_MAXLEN];//ȫ·����
	char   userstr[DEFAULT_DESCRIBE_LENG];//�û�������Զ����ִ�(ԭ�����ʽ����)
}Item_UploadFileDetailInfo, *pItem_UploadFileDetailInfo;

//call return
//method: query
typedef struct
{
	Type_HeaderInfo hdr;
	int32u			num;
}Type_UploadFileDetailInfo, *pType_UploadFileDetailInfo;

//ɾ���ϴ�
//call
//type:TYPE_MEET_INTERFACE_UPLOAD
//method: del
typedef struct
{
	Type_HeaderInfo hdr;

	int32u  mediaid;//ý��ID
}Type_DelUploadFile, *pType_DelUploadFile;

//��ѯý���ļ��ı��롢֡�ʵ���Ϣ
//call
//type:TYPE_MEET_INTERFACE_UPLOAD
//method: detail
typedef struct
{
	Type_HeaderInfo hdr;

	//----in
	char  filepathname[MEET_PATHNAME_MAXLEN];//�ļ�ȫ·����

	//----out
	int32u mSec;//�ļ�����ʱ�� 
	int64u bitrate;//�ļ�������-������ ��λ�� kbps 

	//audio
	int   audiocodecid;// ffmpeg��Ӧ�ı���ID
	char  audiocodecname[64];//��Ƶ����������
	int   samplerate;
	int   channel;

	//video
	int   videocodecid;// ffmpeg��Ӧ�ı���ID
	char  videocodecname[64];//��ƵƵ����������
	float framepersecond;//��Ƶ֡�� fps
	int   width;//��Ƶͼ���ԭʼ���
	int   height;//��Ƶͼ���ԭʼ�߶�
}Type_FileCodecDetailInfo, *pType_FileCodecDetailInfo;

//������Ա
//callback
//TYPE_MEET_INTERFACE_PEOPLE
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;
	int32u opermethod;//����֪ͨ����Ϊopermethod����������
	int32u id;//���ָ����ID�ձ�ʾ�Ƕ��ض�ID�Ĳ���,Ϊ0��ʾȫ��

}Type_PeopleInfo, *pType_PeopleInfo;

//������Ա
typedef struct
{
	int32u personid;
	char   name[DEFAULT_NAME_MAXLEN];
	char   company[DEFAULT_DESCRIBE_LENG];
	char   job[DEFAULT_DESCRIBE_LENG];
	char   comment[DEFAULT_DESCRIBE_LENG];
	char   phone[DEFAULT_SHORT_DESCRIBE_LENG];
	char   email[DEFAULT_SHORT_DESCRIBE_LENG];
	char   password[DEFAULT_PASSWORD_LENG];
}Item_PersonDetailInfo, *pItem_PersonDetailInfo;

//call return
//TYPE_MEET_INTERFACE_PEOPLE
//method: add\del\modify\query
typedef struct
{
	Type_HeaderInfo hdr;
	int32u			personnum;
}Type_PersonDetailInfo, *pType_PersonDetailInfo;

//������Ա����
//callback
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;
	int32u opermethod;//����֪ͨ����Ϊopermethod����������
	int32u id;//���ָ����ID�ձ�ʾ�Ƕ��ض�ID�Ĳ���,Ϊ0��ʾȫ��

}Type_PeopleGroup, *pType_PeopleGroup;

//������Ա����
typedef struct
{
	int32u groupid;
	char   name[DEFAULT_NAME_MAXLEN];
}Item_PeopleGroupDetailInfo, *pItem_PeopleGroupDetailInfo;

//method: add\del\modify\query
typedef struct
{
	Type_HeaderInfo hdr;
	int32u			num;
}Type_PeopleGroupDetailInfo, *pType_PeopleGroupDetailInfo;

//������Ա������Ա
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;
	int32u opermethod;//����֪ͨ����Ϊopermethod����������
	int32u id;//���ָ����ID�ձ�ʾ�Ƕ��ض�ID�Ĳ���,Ϊ0��ʾȫ��
	int32u subid;//���ָ����ID�ձ�ʾ�Ƕ��ض���ԱID�Ĳ���,Ϊ0��ʾ�����ڵ�ȫ����Ա 
}Type_PeopleInGroup, *pType_PeopleInGroup;

//������Ա������Ա
typedef struct
{
	int32u memberid;
}Item_PeopleInGroupDetailInfo, *pItem_PeopleInGroupDetailInfo;

//call callreturn
//method: save\query\move\del
typedef struct
{
	Type_HeaderInfo hdr;

	int32u			groupid;
	int32u			num;
}Type_PeopleInGroupDetailInfo, *pType_PeopleInGroupDetailInfo;

//��ӳ�����Ա������Ա
//call
//method: add
typedef struct
{
	Type_HeaderInfo hdr;

	int32u			groupid;
	int			    num;//
	//Item_PersonDetailInfo item[num];
}Type_AddPeopleToGroup, *pType_AddPeopleToGroup;

//�λ���Ա
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;
	int32u opermethod;//����֪ͨ����Ϊopermethod����������
	int32u id;//���ָ����ID�ձ�ʾ�Ƕ��ض�ID�Ĳ���,Ϊ0��ʾȫ��

}Type_MemberInfo, *pType_MemberInfo;

//�λ���Ա
typedef struct
{
	int32u personid;
	char   name[DEFAULT_NAME_MAXLEN];
	char   company[DEFAULT_DESCRIBE_LENG];
	char   job[DEFAULT_DESCRIBE_LENG];
	char   comment[DEFAULT_DESCRIBE_LENG];
	char   phone[DEFAULT_SHORT_DESCRIBE_LENG];
	char   email[DEFAULT_SHORT_DESCRIBE_LENG];
	char   password[DEFAULT_PASSWORD_LENG];
}Item_MemberDetailInfo, *pItem_MemberDetailInfo;

//method: add\del\modify\query
typedef struct
{
	Type_HeaderInfo hdr;
	int32u			personnum;
}Type_MemberDetailInfo, *pType_MemberDetailInfo;

//�޸Ĳλ���Ա����
//call
//type: TYPE_MEET_INTERFACE_MEMBER
//method: METHOD_MEET_INTERFACE_CONTROL
typedef struct
{
	Type_HeaderInfo hdr;
	int32u			membernum;
	//int32u		memberid[];//��ID����
}Type_ModifyMemberPos, *pType_ModifyMemberPos;

//memberdetailflag
#define MEMBERDETAIL_FLAG_ONLINE 0x00000001 //�Ƿ�����

//�λ���Ա��ϸ��Ϣ
typedef struct
{
	int32u devid;
	char   devname[DEVICE_NAME_MAXLEN];
	int32u memberdetailflag;//��־ �μ�memberdetailflag ����
	int32u facestatus;//����״̬ �μ�MemState_MainFace ����
	
	int32u memberid;
	char   membername[DEFAULT_NAME_MAXLEN];
	
	int32u permission;//�λ���ԱȨ�� �μ�memperm_sscreen ����
}Item_MeetMemberDetailInfo, *pItem_MeetMemberDetailInfo;

//��ȡ�λ���Ա��ϸ��Ϣ,ע����Ҫ����λ��ˡ��λ���Ȩ�ޡ���λ����
//call
//type: TYPE_MEET_INTERFACE_MEMBER
//method: METHOD_MEET_INTERFACE_DETAILINFO
typedef struct
{
	Type_HeaderInfo hdr;

	int32u			num;
	//Item_MeetMemberDetailInfo [num]
}Type_MeetMemberDetailInfo, *pType_MeetMemberDetailInfo;

#define MEET_SMS_OPERTEMP_START  0//=����֪ͨ
#define MEET_SMS_OPERTEMP_LATE   1//=��������
#define MEET_SMS_OPERTEMP_CANCLE 2//=����ȡ��
//����֪ͨ����
//call
//type��TYPE_MEET_INTERFACE_MEMBER
//fun: FUN_All �������֪ͨ
//method:METHOD_MEET_INTERFACE_NOTIFY
typedef struct
{
	Type_HeaderInfo hdr;
	int    templateindex;//=0����֪ͨ, =1��������, =2����ȡ�� ��Ҫָ��ģ�壬��̨�����ģ��ȥ����
}Type_MeetSMSNotify, *pType_MeetSMSNotify;

//��ѯָ���λ���Ա��ĳ������
//property id
#define MEETMEMBER_PROPERTY_NAME				1 //���� query(string)
#define MEETMEMBER_PROPERTY_COMPANY				2 //��˾ query(string)
#define MEETMEMBER_PROPERTY_JOB					3 //��� query(string)
#define MEETMEMBER_PROPERTY_COMMENT				4 //��ע query(string)
#define MEETMEMBER_PROPERTY_PHONE				5 //�绰 query(string)
#define MEETMEMBER_PROPERTY_EMAIL				6 //���� query(string)
#define MEETMEMBER_PROPERTY_PASSWORD			7 //���� query(string)
#define MEETMEMBER_PROPERTY_POS				    8 //������� query(int32u)

#define MEET_MEMBERSTRING_MAXLEN 260
//method: queryproperty
typedef struct
{
	Type_HeaderInfo hdr;

	int32u propertyid;//����ID
	int32u parameterval;//������� Ϊ0��ʾ�������ö�����Աid
	char   propertytext[MEET_MEMBERSTRING_MAXLEN];//�ַ���

}Type_MeetMemberQueryProperty, *pType_MeetMemberQueryProperty;

//method: queryproperty
typedef struct
{
	Type_HeaderInfo hdr;

	int32u propertyid;//����ID
	int32u parameterval;//������� Ϊ0��ʾ�������ö�����Աid
	char   propertyval;//���ص�����ֵ

}Type_MeetMemberQueryPropertyInt32u, *pType_MeetMemberQueryPropertyInt32u;

//�λ���ԱȨ��
//type:TYPE_MEET_INTERFACE_MEMBERPERMISSION
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;
	int32u opermethod;//����֪ͨ����Ϊopermethod����������
	int32u id;//���ָ����ID�ձ�ʾ�Ƕ��ض�ID�Ĳ���,Ϊ0��ʾȫ��

}Type_MemberPerMissionInfo, *pType_MemberPerMissionInfo;

//�λ���Ȩ��
#define memperm_sscreen					0x00000001		//ͬ��Ȩ��
#define memperm_projective				0x00000002		//ͶӰȨ��
#define memperm_upload					0x00000004		//�ϴ�Ȩ��
#define memperm_download				0x00000008		//����Ȩ��
#define memperm_vote					0x00000010		//ͶƱȨ��
#define memperm_postilview				0x00000020		//��ע�鿴Ȩ�� -- �����浽���ݿ�

typedef struct
{
	unsigned int memberid;
	unsigned int permission;	//�λ���Ȩ��
}Item_MemberPermission, *pItem_MemberPermission;

//type:TYPE_MEET_INTERFACE_MEMBERPERMISSION
//method: save\query
typedef struct
{
	Type_HeaderInfo hdr;
	int32u			num;
}Type_MemberPermission, *pType_MemberPermission;

//��ѯָ����Ա��Ȩ��
//property id
#define MEETMEMBERPERMISSION_PROPERTY_ALLPERMISSIONS	1 //����Ȩ�� query
#define MEETMEMBERPERMISSION_PROPERTY_SCREEN			2 //ͬ��Ȩ�� query
#define MEETMEMBERPERMISSION_PROPERTY_PROJECTIVE		3 //ͶӰȨ�� query
#define MEETMEMBERPERMISSION_PROPERTY_UPLOAD			4 //�ϴ�Ȩ�� query
#define MEETMEMBERPERMISSION_PROPERTY_DOWNLOAD			5 //����Ȩ�� query
#define MEETMEMBERPERMISSION_PROPERTY_VOTE				6 //ͶƱȨ�� query

//type:TYPE_MEET_INTERFACE_MEMBERPERMISSION
//method: queryproperty
typedef struct
{
	Type_HeaderInfo hdr;

	int32u propertyid;//����ID
	int32u memberid;//������� Ϊ0��ʾ�����豸ID

	int32u propertyval; //����
}Type_MemberPermissionQueryProperty, *pType_MemberPermissionQueryProperty;

//�λ���Ա����
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;
	int32u opermethod;//����֪ͨ����Ϊopermethod����������
	int32u id;//���ָ����ID�ձ�ʾ�Ƕ��ض�ID�Ĳ���,Ϊ0��ʾȫ��

}Type_MemberGroup, *pType_MemberGroup;

//�λ���Ա����
typedef struct
{
	int32u groupid;
	char   name[DEFAULT_NAME_MAXLEN];
}Item_MemberGroupDetailInfo, *pItem_MemberGroupDetailInfo;

//method: add\del\modify\query
typedef struct
{
	Type_HeaderInfo hdr;
	int32u			num;
}Type_MemberGroupDetailInfo, *pType_MemberGroupDetailInfo;

//�λ���Ա������Ա
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;
	int32u opermethod;//����֪ͨ����Ϊopermethod����������
	int32u id;//���ָ����ID�ձ�ʾ�Ƕ��ض�ID�Ĳ���,Ϊ0��ʾȫ��
	int32u subid;
}Type_MemberGroupItem, *pType_MemberGroupItem;

//�λ���Ա������Ա
typedef struct
{
	int32u memberid;
}Item_MemberInGroupDetailInfo, *pItem_MemberInGroupDetailInfo;

//method: save\query
typedef struct
{
	Type_HeaderInfo hdr;

	int32u			groupid;
	int32u			num;
}Type_MemberInGroupDetailInfo, *pType_MemberInGroupDetailInfo;

//�豸������Ϣ
//method: notify refresh
typedef struct
{
	Type_HeaderInfo hdr;

}Type_DeviceFaceShow, *pType_DeviceFaceShow;

//----------����ǩ������----------
#define signin_direct	0x00		//����--ֱ��ǩ��
#define signin_psw		0x01		//����--��������ǩ��
#define signin_photo	0x02		//����--����(��д)ǩ��
#define signin_onepsw	0x03		//����--��������ǩ��
#define signin_onepsw_photo	0x04	//����--��������+����(��д)ǩ��
#define signin_psw_photo	0x05	//����--��������+����(��д)ǩ��
//method: query
typedef struct
{
	Type_HeaderInfo hdr;

	int32u deviceid;		//�豸ID
	int32u meetingid;		//����ID
	int32u memberid;		//��ԱID
	int32u roomid;			//�᳡ID
	int8u  signin_type;
	char meetingname[DEFAULT_DESCRIBE_LENG];	//��������
	char membername[DEFAULT_NAME_MAXLEN];     //��Ա����
	char company[DEFAULT_DESCRIBE_LENG];		//��˾����
	char job[DEFAULT_DESCRIBE_LENG];			//ְλ����
}Type_DeviceFaceShowDetail, *pType_DeviceFaceShowDetail;

//�᳡��Ϣ
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;
	int32u opermethod;//����֪ͨ����Ϊopermethod����������
	int32u id;//���ָ����ID�ձ�ʾ�Ƕ��ض�ID�Ĳ���,Ϊ0��ʾȫ��

}Type_MeetRoom, *pType_MeetRoom;

//�᳡��Ϣ
typedef struct
{
	int32u roomid;		
	int32u roombgpicid;	
	char name[DEFAULT_DESCRIBE_LENG];	
	char addr[DEFAULT_DESCRIBE_LENG];    
	char comment[DEFAULT_DESCRIBE_LENG];
}Item_MeetRoomDetailInfo, *pItem_MeetRoomDetailInfo;

//method: save\query
typedef struct
{
	Type_HeaderInfo hdr;

	int32u			num;
}Type_MeetRoomDetailInfo, *pType_MeetRoomDetailInfo;

//��ѯ�᳡������Ϣ
//property id
#define MEETROOM_PROPERTY_BGPHOTOID			1 //���᳡ID���ػ᳡��ͼID query
#define MEETSEAT_PROPERTY_CANMANAGER		2 //��ѯ����Ա�Ƿ���Կ���ָ���᳡ query(parameterval=roomid, parameterval2=adminid)

//method: queryproperty
typedef struct
{
	Type_HeaderInfo hdr;

	int32u propertyid;//����ID
	int32u parameterval;//������� Ϊ0��ʾ��ǰ�᳡��Ӧ����Ϣ
	int32u parameterval2;//������� 
	int32u propertyval;//ֵ 

}Type_MeetRoomQueryProperty, *pType_MeetRoomQueryProperty;

//�᳡�豸��Ϣ
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;
	int32u opermethod;//����֪ͨ����Ϊopermethod����������
	int32u id;//���ָ����ID�ձ�ʾ�Ƕ��ض�ID�Ĳ���,Ϊ0��ʾȫ��
	int32u subid;
}Type_MeetRoomDevice, *pType_MeetRoomDevice;

//�᳡�豸��Ϣ
//method: add \ del
typedef struct
{
	Type_HeaderInfo hdr;

	int32u   roomid;//�᳡ID
	int32u	 num; //�豸��
}Type_MeetRoomModDeviceInfo, *pType_MeetRoomModDeviceInfo;

//direction �᳡�豸����
#define SEATDIRECTION_UP	0 //����
#define SEATDIRECTION_DOWN	1 //����
#define SEATDIRECTION_LEFT	2 //����
#define SEATDIRECTION_RIGHT 3 //����

//�᳡�豸��Ϣ
typedef struct
{
	int32u devid;
	float  x;
	float  y;
	int    direction;//�᳡�豸����
}Item_MeetRoomDevPosInfo, *pItem_MeetRoomDevPosInfo;

//TYPE_MEET_INTERFACE_ROOMDEVICE
//method: query/set
typedef struct
{
	Type_HeaderInfo hdr;

	int32u   roomid;//�᳡ID
	int32u	 num; //�豸��
}Type_MeetRoomDevPosInfo, *pType_MeetRoomDevPosInfo;

//�᳡�豸��λ��ϸ��Ϣ
typedef struct
{
	int32u devid;
	char   devname[DEVICE_NAME_MAXLEN];
	float  x;
	float  y;
	int    direction;//�᳡�豸����

	int32u memberid;
	char   membername[DEFAULT_NAME_MAXLEN];

	int  issignin;//�Ƿ��Ѿ�ǩ�� 
	int  role;//�λ���Ա��ɫ 

	int32u	facestate;//����״̬ �μ�MemState_MainFace ����
}Item_MeetRoomDevSeatDetailInfo, *pItem_MeetRoomDevSeatDetailInfo;

//type:TYPE_MEET_INTERFACE_ROOMDEVICE
//method: METHOD_MEET_INTERFACE_DETAILINFO, MAKEVIDEO
typedef struct
{
	Type_HeaderInfo hdr;

	int32u   roomid;//�᳡ID
	int32u	 num; //�豸��
}Type_MeetRoomDevSeatDetailInfo, *pType_MeetRoomDevSeatDetailInfo;

//�᳡�豸��Ϣ
//filterflag
#define MEET_ROOMDEVICE_FLAG_ONLINE		0x0000001 //����
#define MEET_ROOMDEVICE_FLAG_FACESTATUS 0x0000002 //ƥ��������״̬
#define MEET_ROOMDEVICE_FLAG_BINDMEMBER 0x0000004 //�λ���Ա��
#define MEET_ROOMDEVICE_FLAG_PROJECTIVE 0x0000008 //ͶӰ��
#define MEET_ROOMDEVICE_FLAG_HADSIGNIN  0x0000010 //�Ѿ�ǩ��
#define MEET_ROOMDEVICE_FLAG_NOSELFID   0x0000020 //�ų�����ID
#define MEET_ROOMDEVICE_FLAG_NODEVID    0x0000040 //�ų�ָ��ID
#define MEET_ROOMDEVICE_FLAG_MEMBERSORT 0x0000080 //���λ���Ա����

//��ѯ����Ҫ����豸ID
//call
//type:TYPE_MEET_INTERFACE_ROOMDEVICE
//method: complexquery
typedef struct
{
	Type_HeaderInfo hdr;

	int32u   filterflag;//��ѯ��־
	int32u   roomid;//�᳡ID

	int32u   facestatus; //ƥ��������״̬
	int32u   exceptdevid;//�ų�ָ��ID
}Type_DumpMeetRoomDevInfo, *pType_DumpMeetRoomDevInfo;

typedef struct
{
	int32u devid;
}Item_ResMeetRoomDevInfo, *pItem_ResMeetRoomDevInfo;

//���ط���Ҫ����豸ID
//callreturn
//type:TYPE_MEET_INTERFACE_ROOMDEVICE
//method: complexquery
typedef struct
{
	Type_HeaderInfo hdr;

	int32u   queryflag;//��ѯ��־

	int32u   facestatus;
	int32u   exceptdevid;

	int32u   roomid;//�᳡ID
	int32u	 num;   //�豸��
}Type_ResMeetRoomDevInfo, *pType_ResMeetRoomDevInfo;

//�᳡��ͼ��Ϣ 
//type:TYPE_MEET_INTERFACE_ROOM
//method: set
typedef struct
{
	Type_HeaderInfo hdr;

	int32u   roomid;//�᳡ID

	//���bgpicidָ��, ����Ҫָbgpathname ���ָ��bgpathname������Ҫָbgpicid ���ͬʱָ��,������ʹ��bgpicid
	int32u	 bgpicid; //��ͼý��ID ���ָ��bgpathname���ϴ��ɹ����ֵ�᷵�ػ᳡��ý��ID
	char	 bgpathname[MEET_PATHNAME_MAXLEN];//���᳡��һ���µ�ͼƬ
}Type_MeetRoomModBGInfo, *pType_MeetRoomModBGInfo;

//������Ϣ
//type:TYPE_MEET_INTERFACE_MEETINFO
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;
	int32u opermethod;//����֪ͨ����Ϊopermethod����������
	int32u id;//���ָ����ID�ձ�ʾ�Ƕ��ض�ID�Ĳ���,Ϊ0��ʾȫ��

}Type_MeetInfo, *pType_MeetInfo;

//status ����״̬
#define 	MEETING_STATUS_Ready  0//���鴴���У��ȴ���ʼ
#define 	MEETING_STATUS_Start  1//���鿪ʼ������
#define 	MEETING_STATUS_End    2//�������
#define 	MEETING_STATUS_PAUSE  3//������ͣ
#define 	MEETING_STATUS_Model  4//ģ�����

#define SIGNIN_PSW_LEN 16
//������Ϣ
typedef struct
{
	int32u			id; //����ID
	char			name[DEFAULT_DESCRIBE_LENG]; //����
	int32u			roomId; //�᳡ID���������� 
	char			roomname[DEFAULT_DESCRIBE_LENG]; //���Ʋ�ѯ������Ч,���������ʹ��,Ҳ����Ҫ��ֵ
	int				secrecy; //�Ƿ�Ϊ���ܻ��� 1Ϊ�ܱ�����
	int64u			startTime; //��ʼʱ�� ��λ:��
	int64u			endTime;   //����ʱ�� ��λ:��
	int8u			signin_type;//ǩ������
	int32u			managerid;//����Աid
	char			onepsw_signin[SIGNIN_PSW_LEN];//����ǩ������
	int32u			status;//����״̬��0Ϊδ��ʼ���飬1Ϊ�ѿ�ʼ���飬2Ϊ�ѽ�������
	char		    ordername[DEFAULT_NAME_MAXLEN];//����ԤԼ������
}Item_MeetMeetInfo, *pItem_MeetMeetInfo;

//type:TYPE_MEET_INTERFACE_MEETINFO
//method: add/mod/del/query/dump
typedef struct
{
	Type_HeaderInfo hdr;
	int32u	 num; //
}Type_MeetMeetInfo, *pType_MeetMeetInfo;

//complex query meet flag
#define MEET_COMPLEXQUERY_FLAG_MEETINGID 0x00000001 //meetingid��Ч �ñ�־��Ч������������־
#define MEET_COMPLEXQUERY_FLAG_STATUS    0x00000002 //status��Ч
#define MEET_COMPLEXQUERY_FLAG_TIME	     0x00000004 //startutctime endutctime��Ч
#define MEET_COMPLEXQUERY_FLAG_ROOMID	 0x00000008 //roomid��Ч 
#define MEET_COMPLEXQUERY_FLAG_CACHE	 0x00000010 //��������������� ֻ����MEET_COMPLEXQUERY_FLAG_MEETINGID����Ч
#define MEET_COMPLEXQUERY_FLAG_PHONE	 0x00000020 //������Ա�绰 

//complex query cache flag �����µ�˳�򷵻�
#define MEET_COMPLEXCACHE_FLAG_MEETFUNCTION			0x0000000000000001 //���ͻ��鹦��
#define MEET_COMPLEXCACHE_FLAG_MEETAGENDA			0x0000000000000002 //���ͻ������
#define MEET_COMPLEXCACHE_FLAG_MEETBULLET			0x0000000000000004 //���ͻ��鹫��
#define MEET_COMPLEXCACHE_FLAG_MEMBER				0x0000000000000008 //���Ͳλ���Ա
#define MEET_COMPLEXCACHE_FLAG_MEMBERPERMISSION		0x0000000000000010 //���Ͳλ���ԱȨ��
#define MEET_COMPLEXCACHE_FLAG_MEMBERSEAT			0x0000000000000020 //���ͻ�����λ
#define MEET_COMPLEXCACHE_FLAG_MEETVIDEO			0x0000000000000040 //���ͻ�����Ƶֱ��
#define MEET_COMPLEXCACHE_FLAG_MEETVOTE				0x0000000000000080 //���ͻ���ͶƱ
#define MEET_COMPLEXCACHE_FLAG_DIRFILE				0x0000000000000100 //���ͻ���Ŀ¼��Ŀ¼�ļ�
#define MEET_COMPLEXCACHE_FLAG_ROOMDEVICE			0x0000000000000200 //���ͻ᳡��᳡�豸
#define MEET_COMPLEXCACHE_FLAG_MEETSIGN				0x0000000000000400 //���ͻ���ǩ��
#define MEET_COMPLEXCACHE_FLAG_TABLECARD			0x0000000000000800 //���ͻ�������

//���ϲ�ѯ����
//type:TYPE_MEET_INTERFACE_MEETINFO
//method: complexquery
typedef struct
{
	Type_HeaderInfo hdr;

	int32u		 queryflag;	//��ѯ��־λ MEET_COMPLEXQUERY_FLAG_MEETINGID
	int32u		 meetingid;//����ID
	int64u		 cacheflag;//�����־ MEET_COMPLEXCACHE_FLAG_DIRFILE
	int32u		 roomid;//�᳡ID
	int32u	     status;//����״̬��0Ϊδ��ʼ���飬1Ϊ�ѿ�ʼ���飬2Ϊ�ѽ�������
	int64u		 startutctime;//��ʼʱ�� ��λ��UTC��
	int64u		 endutctime;//����ʱ�� ��λ��UTC��
	char		 phone[DEFAULT_SHORT_DESCRIBE_LENG];  //������Ա�绰
}Type_ComplexQueryMeetInfo, *pType_ComplexQueryMeetInfo;

//��ѯ����������Ϣ
//property id
#define MEET_PROPERTY_SECRECY			1 //������ID���ػ����Ƿ�Ϊ���ܻ��� query
#define MEET_PROPERTY_ROOMID			2 //������ID���ػ���᳡ID query
#define MEET_PROPERTY_SIGNINTYPE		3 //������ID���ػ���ǩ����ʽ query
#define MEET_PROPERTY_MANAGERID			4 //������ID���ػ������ԱiD query
#define MEET_PROPERTY_STATUS			5 //������ID���ػ���״̬  query
#define MEET_PROPERTY_STARTTIME			6 //������ID���ػ��鿪ʼʱ�� query
#define MEET_PROPERTY_ENDTIME			7 //������ID���ػ������ʱ�� query

//type:TYPE_MEET_INTERFACE_MEETINFO
//method: queryproperty
typedef struct
{
	Type_HeaderInfo hdr;

	int32u propertyid;//����ID
	int32u parameterval;//������� Ϊ0��ʾ��ǰ����
	int32u propertyval;//ֵ 

}Item_MeetMeetInfoQueryProperty_int32u, *pItem_MeetMeetInfoQueryProperty_int32u;

//method: queryproperty
typedef struct
{
	Type_HeaderInfo hdr;

	int32u propertyid;//����ID
	int32u parameterval;//������� Ϊ0��ʾ��ǰ����
	int64u propertyval;//ֵ 

}Item_MeetMeetInfoQueryProperty_int64u, *pItem_MeetMeetInfoQueryProperty_int64u;

//�޸Ļ���״̬
//type:TYPE_MEET_INTERFACE_MEETINFO
//method: modifystatus
typedef struct
{
	Type_HeaderInfo hdr;
	int32u  meetid;			//����ID
	int32u  status; //����״̬

}Type_MeetModStatus, *pType_MeetModStatus;

//����Ŀ¼
//type:TYPE_MEET_INTERFACE_MEETDIRECTORY
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;
	int32u opermethod;//����֪ͨ����Ϊopermethod����������
	int32u id;//���ָ����ID�ձ�ʾ�Ƕ��ض�ID�Ĳ���,Ϊ0��ʾȫ��

}Type_MeetDirectory, *pType_MeetDirectory;

//����Ŀ¼
typedef struct
{
	int32u			id; //Ŀ¼ID
	char			name[DEFAULT_DESCRIBE_LENG]; //����
	int32u			parentid; //��Ŀ¼ID
	int32u			dirpos; //Ŀ¼���
	int			    filenum;//��Ŀ¼�µ��ļ�����
}Item_MeetDirDetailInfo, *pItem_MeetDirDetailInfo;

//type:TYPE_MEET_INTERFACE_MEETDIRECTORY
//method: add/mod/del/query
typedef struct
{
	Type_HeaderInfo hdr;
	int32u	 num; //
}Type_MeetDirDetailInfo, *pType_MeetDirDetailInfo;

typedef struct
{
	int32u dirid;  //����Ŀ¼
	int32u pos;    //���
}Item_MeetingDirPosItem, *pItem_MeetingDirPosItem;

//�޸Ļ���Ŀ¼����
//type:TYPE_MEET_INTERFACE_MEETDIRECTORY
//method: set
typedef struct
{
	Type_HeaderInfo hdr;
	int32u num;//Ŀ¼��
	//Item_MeetingDirPosItem  item[];//���� 

}Type_ModMeetDirPos, *pType_ModMeetDirPos;

//��ѯָ������Ŀ¼��ĳ������
//property id
#define MEETDIRECTORY_PROPERTY_NAME   1 //Ŀ¼����   query(text)
#define MEETDIRECTORY_PROPERTY_SIZE	  2 //�ļ�����   query(fixed32) 
#define MEETDIRECTORY_PROPERTY_PARENT 3 //��Ŀ¼ID   query(fixed32) 
#define MEETDIRECTORY_PROPERTY_POS    4 //Ŀ¼����� query(fixed32)
#define MEETDIRECTORY_PROPERTY_MAXDIRID   5 //��ǰ��������Ŀ¼ID query(fixed32)

//method: queryproperty
typedef struct
{
	Type_HeaderInfo hdr;

	int32u propertyid;//����ID 
	int32u dirid;//�������
	int32u propertyval;//����ֵ

}Type_MeetDirectoryQueryPropertyInt32u, *pType_MeetDirectoryQueryPropertyInt32u;

#define MEET_DIRECTORYSTRING_MAXLEN 260
//method: queryproperty
typedef struct
{
	Type_HeaderInfo hdr;

	int32u propertyid;//����ID
	int32u dirid;//������� Ϊ0��ʾ��ǰ����
	char   propertytext[MEET_DIRECTORYSTRING_MAXLEN]; //����

}Type_MeetDirectoryQueryPropertyString, *pType_MeetDirectoryQueryPropertyString;

//����Ŀ¼�ļ�
//type:TYPE_MEET_INTERFACE_MEETDIRECTORYFILE
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;
	int32u opermethod;//����֪ͨ����Ϊopermethod����������
	int32u id;//���ָ����ID�ձ�ʾ�Ƕ��ض�ID�Ĳ���,Ϊ0��ʾȫ��
	int32u subid;
}Type_MeetDirectoryFile, *pType_MeetDirectoryFile;

///start FileScore
typedef struct
{
	int32u fileid;  //�ļ�ID
	int32u memberid;//�λ���ԱID
	int32u score;//����
	int64u scoretime;//����utcʱ�� ΢��
}Item_MeetFileScore, *pItem_MeetFileScore;

//�����ļ�����  �޸�
//�����ļ��������
//call\callback
//type:TYPE_MEET_INTERFACE_FILESCORE
//method:��ӡ��޸ġ�ɾ������ѯ(���ݿⷵ��)
typedef struct
{
	Type_HeaderInfo hdr;
	int		 isfirst;//�Ƿ�Ϊ��һ֡,�������̫��,��������֡�����´ν��յ���ص�
	int32u	 num; //pItem_MeetFileScore
	pItem_MeetFileScore pitem;
}Type_MeetFileScore, *pType_MeetFileScore;

//�����ļ�����  ��ѯ
//�����ļ��������
//call
//type:TYPE_MEET_INTERFACE_FILESCORE
//method:METHOD_MEET_INTERFACE_ASK\METHOD_MEET_INTERFACE_QUERY
typedef struct
{
	Type_HeaderInfo hdr;

	int32u fileid;   //�ļ�ID
}Type_QueryFileScore, *pType_QueryFileScore;

//�����ļ����ֱ��֪ͨ
//�����ļ��������
//callback
//type:TYPE_MEET_INTERFACE_FILESCORE
//method:��ӡ��޸ġ�ɾ��
typedef struct
{
	Type_HeaderInfo hdr;
	Item_MeetFileScore item;
}Type_MeetFileScoreNotify, *pType_MeetFileScoreNotify;

//�����ļ�����  �����ļ�ƽ���ֲ�ѯ���
//�����ļ��������
//callback
//type:TYPE_MEET_INTERFACE_FILESCORE
//method:METHOD_MEET_INTERFACE_ASK
typedef struct
{
	Type_HeaderInfo hdr;

	int32u fileid;   //�ļ�ID
	int32u score;	 //ƽ������
}Type_QueryAverageFileScore, *pType_QueryAverageFileScore;
///end FileScore

#define MAX_EVALUATETEXTLEN 260 //���۵��ı���󳤶�
//evaluate flag
#define FILEEVALUATE_FLAG_SECRETARY 0x00000001 //�ñ�־Ϊ1��ʾ���۶��ⲻ�ɼ�

///start Fileevaluate
typedef struct
{
	int32u fileid;  //�ļ�ID
	int32u memberid;//�λ���ԱID
	int32u flag;//��־ �μ�evaluate flag �궨��
	int64u evaluatetime;//����utcʱ�� ΢��
	char   evaluate[MAX_EVALUATETEXTLEN];//���ֵ��ı�
}Item_FileEvaluate, *pItem_FileEvaluate;

//�����ļ����� 
//type��TYPE_MEET_INTERFACE_FILEEVALUATE
//method:add��query
typedef struct
{
	Type_HeaderInfo hdr;

	int32u   totalrecord;//�����������ܼ�¼����
	int32u   totalnum;//���β�ѯ��֡���ܼ�¼��
	int32u   startrow;//��ѯ�����û��������Ŀ�ʼ��
	int		 isfirst;//�Ƿ�Ϊ��һ֡,�������̫��,��������֡�����´ν��յ���ص�
	int32u	 num; //pItem_FileEvaluate
	pItem_FileEvaluate pitem;
}Type_MeetingFileEvaluate, *pType_MeetingFileEvaluate;

//�����ļ�����  ��ѯ
//type��TYPE_MEET_INTERFACE_FILEEVALUATE
//method:query
typedef struct
{
	Type_HeaderInfo hdr;

	int32u fileid;   //�ļ�ID ����Ϊ0��ʾ�����ļ�
	int32u memberid;//�λ���ԱID ����Ϊ0��ʾ���вλ���
	int64u startevaluatetime;//��ѯ����ʼ����utcʱ�� ΢�� ������Ч
	int64u endevaluatetime;//��ѯ�Ľ�������utcʱ�� ΢�� ������Ч
	int32u startrow;//��ѯ��ʼ�� ʵ�ַ�ҳ��ѯ ������Ч ��һ�δ�0��ʼ
}Type_QueryFileEvaluate, *pType_QueryFileEvaluate;

//�����ļ����� ���ӡ��޸ĵı��֪ͨ
//callback
//type��TYPE_MEET_INTERFACE_FILEEVALUATE
//method:add\modify
typedef struct
{
	Type_HeaderInfo hdr;

	Item_FileEvaluate item;
}Type_MeetingFileEvaluateNotify, *pType_MeetingFileEvaluateNotify;

//�����ļ�����  ɾ��
//call\callback
//type��TYPE_MEET_INTERFACE_FILEEVALUATE
//method:del
typedef struct
{
	Type_HeaderInfo hdr;

	int32u fileid;   //�ļ�ID ����Ϊ0��ʾ�����ļ�
	int32u memberid;//�λ���ԱID ����Ϊ0��ʾ���вλ���
	int64u evaluatetime;//����utcʱ�� ΢�� ��fileid��memberid��Чʱ ������Ч��ʾɾ��ָ��������
}Type_DelFileEvaluate, *pType_DelFileEvaluate;
///end Fileevaluate

//////////////////////////////////////////////////////////////////////////
///start Meetevaluate
typedef struct
{
	int32u memberid;//�λ���ԱID
	int32u flag;//��־ �μ�file evaluate flag �궨��
	int64u evaluatetime;//����utcʱ�� ΢��
	char   evaluate[MAX_EVALUATETEXTLEN];//���ֵ��ı�
}Item_MeetEvaluate, *pItem_MeetEvaluate;

//�������� 
//type��TYPE_MEET_INTERFACE_MEETEVALUATE
//method:��ӡ���ѯ(���ݿⷵ��)
typedef struct
{
	Type_HeaderInfo hdr;

	int32u   startrow;//��ѯ�����û��������Ŀ�ʼ��
	int32u   totalrecord;//�����������ܼ�¼����
	int32u   totalnum;//���β�ѯ��֡���ܼ�¼��
	int		 isfirst;//�Ƿ�Ϊ��һ֡,�������̫��,��������֡�����´ν��յ���ص�
	int32u	 num; //pItem_MeetEvaluate
	pItem_MeetEvaluate pitem; 
}Type_MeetingMeetEvaluate, *pType_MeetingMeetEvaluate;

//��������  ��ѯ
//stages��TYPE_MEET_INTERFACE_MEETEVALUATE
//method:��ѯ
typedef struct
{
	Type_HeaderInfo hdr;

	int32u memberid;//�λ���ԱID ����Ϊ0��ʾ���вλ���
	int64u startevaluatetime;//��ѯ����ʼ����utcʱ�� ΢�� ������Ч
	int64u endevaluatetime;//��ѯ�Ľ�������utcʱ�� ΢�� ������Ч
	int32u startrow;//��ѯ��ʼ�� ʵ�ַ�ҳ��ѯ ������Ч ��һ�δ�0��ʼ
}Type_QueryMeetEvaluate, *pType_QueryMeetEvaluate;

//�������� ���ӡ��޸ĵı��֪ͨ
//callback
//type��TYPE_MEET_INTERFACE_MEETEVALUATE
//method:add\modify
typedef struct
{
	Type_HeaderInfo hdr;

	Item_MeetEvaluate item;
}Type_MeetingMeetEvaluateNotify, *pType_MeetingMeetEvaluateNotify;

//��������  ɾ��
//call \callback
//stages��TYPE_MEET_INTERFACE_MEETEVALUATE
//method:ɾ��
typedef struct
{
	Type_HeaderInfo hdr;

	int32u memberid;//�λ���ԱID ����Ϊ0��ʾ���вλ���
	int64u evaluatetime;//����utcʱ�� ΢�� ��memberid��Чʱ ������Ч��ʾɾ��ָ��������
}Type_DelMeetEvaluate, *pType_DelMeetEvaluate;
///end Meetevaluate

//////////////////////////////////////////////////////////////////////////
///start systemlog
#define MEE_MAX_LOGPARAMETER_NUM 4
typedef struct
{
	int32u pageid;//����id  �μ�systemlogoperid.h SYSTEMLOG_PAGEID
	int32u operid;//�������  �μ�systemlogoperid.h  SYSTEMLOG_OPERID
	int32u meetid;//�����Ļ���ID
	int32u roomid;//�����Ļ᳡ID
	int32u deviceid;//�������豸ID
	int32u urole;//��Ա��ɫ �μ�role_admin
	int32u uid;//��ԱID

	int64u opertime;//����utcʱ�� ΢��

	int32u param[MEE_MAX_LOGPARAMETER_NUM];//���ݲ�����Ӧ�Ĳ������������ڿ���ͳ�ƣ�eg:���ŵ��ļ�ID�����ŵ����豸ID,
}Item_MeetSystemLog, *pItem_MeetSystemLog;

//��־��ѯ���� 
//type��TYPE_MEET_INTERFACE_SYSTEMLOG
//method:��ѯ(���ݿⷵ��)
typedef struct
{
	Type_HeaderInfo hdr;

	int32u   totalrecord;//���β�ѯ�ܼ�¼��
	int32u   startrow;//��ѯ�����û��������Ŀ�ʼ��
	int		 isfirst;//�Ƿ�Ϊ��һ֡,�������̫��,��������֡�����´ν��յ���ص�
	int32u	 num; //pItem_MeetSystemLog
	pItem_MeetSystemLog pitem;
}Type_MeetingMeetSystemLog, *pType_MeetingMeetSystemLog;

//�����־ 
//call callback
//type��TYPE_MEET_INTERFACE_SYSTEMLOG
//method:add
typedef struct
{
	Type_HeaderInfo hdr;

	Item_MeetSystemLog item;
}Type_AddMeetSystemLog, *pType_AddMeetSystemLog;

//������־ 
//call
//type��TYPE_MEET_INTERFACE_SYSTEMLOG
//method:��ѯ
typedef struct
{
	Type_HeaderInfo hdr;

	int32u pageid;//����id  �μ�systemlogoperid.h SYSTEMLOG_PAGEID Ϊ0��ʾ����Ϊ��ѯ����
	int32u operid;//�������� �μ�systemlogoperid.h  SYSTEMLOG_OPERID Ϊ0��ʾ����Ϊ��ѯ����
	int32u meetid;//�����Ļ���ID Ϊ0��ʾ����Ϊ��ѯ����
	int32u roomid;//�����Ļ᳡ID Ϊ0��ʾ����Ϊ��ѯ����
	int32u deviceid;//�������豸ID Ϊ0��ʾ����Ϊ��ѯ����
	int32u urole;//��Ա��ɫ �μ�role_admin Ϊ0��ʾ����Ϊ��ѯ����
	int32u uid;//��ԱID Ϊ0��ʾ����Ϊ��ѯ����

	int32u param[MEE_MAX_LOGPARAMETER_NUM];//���ݲ�����Ӧ�Ĳ�������  Ϊ0��ʾ����Ϊ��ѯ����

	int64u startopertime;//��ѯ����ʼ��¼utcʱ�� ΢�� ������Ч
	int64u endopertime;//��ѯ�Ľ�����¼utcʱ�� ΢�� ������Ч
	int32u startrow;//��ѯ��ʼ�� ʵ�ַ�ҳ��ѯ ������Ч
}Type_QueryMeetSystemLog, *pType_QueryMeetSystemLog;
///end systemlog

//----------�λ���Ա��ɫ----------
#define role_member_nouser 		0x00  //δʹ��
#define role_member_normal 		0x01  //һ��λ���Ա
#define role_member_compere 	0x03  //������
#define role_member_secretary 	0x04  //����
#define role_device_projector	0x08  //ͶӰ��
#define role_admin				0x09  //����Ա

//�ļ�attrib
#define MEETFILE_ATTRIB_IMPORTANTFILE 0x000000001 //�����ļ�

//����Ŀ¼�ļ�
typedef struct
{
	int32u			mediaid; //�ļ�ID
	char			name[DEFAULT_FILENAME_LENG]; //�ļ�����
	int32u			uploaderid; //�ϴ���ID
	int8u			uploader_role; //�ϴ��߽�ɫ
	int32u			mstime;//ʱ�� ����
	int64u			size;//��С �ֽ�
	int32u			attrib;//�ļ�����
	int32u			filepos;// �ļ����
	char			uploader_name[DEFAULT_NAME_MAXLEN]; //�ϴ�������
}Item_MeetDirFileDetailInfo, *pItem_MeetDirFileDetailInfo;

//type:TYPE_MEET_INTERFACE_MEETDIRECTORYFILE
//method: add/mod/del/query
typedef struct
{
	Type_HeaderInfo hdr;
	int32u   dirid;
	int32u	 num; //
}Type_MeetDirFileDetailInfo, *pType_MeetDirFileDetailInfo;

//���µ�¼���ļ�ý���ļ�֪ͨ
//type:TYPE_MEET_INTERFACE_MEETDIRECTORYFILE
//callback
//method: add
typedef struct
{
	Type_HeaderInfo hdr;
	int32u mediaid;
	int8u  filename[DEFAULT_FILENAME_LENG];//�ļ���
	int64u len;//��С �ֽ�
	int32u msec; //����ʱ������msΪ��λ,���Ϊ�����ļ� ��¼�����İ汾��Ϣ���ݳ���
}Type_MeetNewRecordFile, *pType_MeetNewRecordFile;

//��ѯָ���ļ���ĳ������
//property id
#define MEETFILE_PROPERTY_NAME   1 //���� query(text)
#define MEETFILE_PROPERTY_SIZE	 2 //��С query(fixed64) �ֽ�
#define MEETFILE_PROPERTY_TIME   3 //ʱ�� query(fixed32) ����
#define MEETFILE_PROPERTY_ATTRIB 4 //�ļ�attrib query(fixed32)
#define MEETFILE_PROPERTY_AVAILABLE 5 //�ļ��Ƿ���� query �����ڷ���ERROR_MEET_INTERFACE_NOFIND
#define MEETFILE_PROPERTY_FILEMD5  6 //�ļ���Ӧ��md5ֵ query(text��32���ֽ�)

//method: queryproperty
typedef struct
{
	Type_HeaderInfo hdr;

	int32u propertyid;//����ID 
	int32u mediaid;//�������
	int32u propertyval;//����ֵ

}Type_MeetMFileQueryPropertyInt32u, *pType_MeetMFileQueryPropertyInt32u;

//method: queryproperty
typedef struct
{
	Type_HeaderInfo hdr;

	int32u propertyid;//����ID
	int32u mediaid;  //�������
	int64u propertyval;//����ֵ

}Type_MeetMFileQueryPropertyInt64u, *pType_MeetMFileQueryPropertyInt64u;

#define MEET_FILESTRING_MAXLEN 260
//method: queryproperty
typedef struct
{
	Type_HeaderInfo hdr;

	int32u propertyid;//����ID
	int32u mediaid;//�������
	char   propertytext[MEET_FILESTRING_MAXLEN];//�ַ���

}Type_MeetMFileQueryPropertyString, *pType_MeetMFileQueryPropertyString;

//�޸Ļ���Ŀ¼�ļ�
typedef struct
{
	int32u			mediaid; //�ļ�ID
	char			name[DEFAULT_FILENAME_LENG]; //�ļ�����
	int32u			attrib;//�ļ����� �μ� owbash.h MEET_FILEATTRIB_BACKGROUND ����
}Item_ModMeetDirFile, *pItem_ModMeetDirFile;

//type:TYPE_MEET_INTERFACE_MEETDIRECTORYFILE
//method: modifyinfo
typedef struct
{
	Type_HeaderInfo hdr;
	int32u   dirid;
	int32u	 num; //
}Type_ModMeetDirFile, *pType_ModMeetDirFile;

//�޸Ļ���Ŀ¼�ļ�����
//type:TYPE_MEET_INTERFACE_MEETDIRECTORYFILE
//method: set
typedef struct
{
	Type_HeaderInfo hdr;

	int32u dirid;//Ŀ¼ID
	int32u filenum;//�ļ���
	//int32u  fileid[];//��ID������ 

}Type_ModMeetDirFilePos, *pType_ModMeetDirFilePos;

//queryflag
#define MEET_FILETYPE_QUERYFLAG_UPLOADID		 0x00000001  //�ϴ�����Ч
#define MEET_FILETYPE_QUERYFLAG_FILETYPE		 0x00000002  //�ļ�������Ч
#define MEET_FILETYPE_QUERYFLAG_ATTRIB			 0x00000004  //�ļ�������Ч
#define MEET_FILETYPE_QUERYFLAG_IMPORTANTFRONT	 0x00000008  //attribΪMEETFILE_ATTRIB_IMPORTANTFILE���ļ��ᱻ����ǰ�淵��

//filetype
#define MEET_FILETYPE_AUDIO		 0x00000001  //��Ƶ
#define MEET_FILETYPE_VIDEO		 0x00000002  //��Ƶ
#define MEET_FILETYPE_PICTURE	 0x00000004  //ͼƬ
#define MEET_FILETYPE_DOCUMENT	 0x00000008  //�ĵ�
#define MEET_FILETYPE_OTHER		 0x00000010  //����
#define MEET_FILETYPE_RECORD	 0x00000020  //¼��
#define MEET_FILETYPE_UPDATE	 0x00000040  //����

//type:TYPE_MEET_INTERFACE_MEETDIRECTORYFILE
//method: complexpagequery
typedef struct
{
	Type_HeaderInfo hdr;
	int32u   dirid;//Ϊ0��ʾ��ƽ̨���ѯ(ƽ̨���ѯ��������� role��uploadid����Ч��)

	int32u   queryflag;//��ѯ��־

	int		 role;//�ϴ��߽�ɫ
	int32u   uploadid;//�ϴ���ԱID Ϊ0��ʾȫ��

	int32u	 filetype;//�ļ����� Ϊ0��ʾȫ��

	int32u   attrib;//�ļ����� Ϊ0��ʾȫ�� �μ� owbash.h MEET_FILEATTRIB_BACKGROUND ����
	int		 pageindex;//��ҳֵ
	int		 pagenum;//��ҳ��С Ϊ0��ʾ����ȫ��
}Type_ComplexQueryMeetDirFile, *pType_ComplexQueryMeetDirFile;

//����Ŀ¼Ȩ��
//type:TYPE_MEET_INTERFACE_MEETDIRECTORYRIGHT
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;
	int32u opermethod;//����֪ͨ����Ϊopermethod����������
	int32u id;//���ָ����ID�ձ�ʾ�Ƕ��ض�ID�Ĳ���,Ϊ0��ʾȫ��

}Type_MeetDirectoryRight, *pType_MeetDirectoryRight;

//����Ŀ¼Ȩ��
typedef struct
{
	int32u			memberid; //�λ���ԱID
}Item_MeetDirRightDetailInfo, *pItem_MeetDirRightDetailInfo;

//type:TYPE_MEET_INTERFACE_MEETDIRECTORYRIGHT
//method: save/query
typedef struct
{
	Type_HeaderInfo hdr;
	int32u   dirid;
	int32u	 num; //
}Type_MeetDirRightDetailInfo, *pType_MeetDirRightDetailInfo;

//������Ƶ
//type:TYPE_MEET_INTERFACE_MEETVIDEO
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;
	int32u opermethod;//����֪ͨ����Ϊopermethod����������
	int32u id;//���ָ����ID�ձ�ʾ�Ƕ��ض�ID�Ĳ���,Ϊ0��ʾȫ��

}Type_MeetVideo, *pType_MeetVideo;

//������Ƶ���
typedef struct
{
	int32u			id; //��ͨ��ID
	int32u		    deviceid;
	char			devname[DEVICE_NAME_MAXLEN];
	int8u			subid;
	char			name[DEFAULT_DESCRIBE_LENG]; //��ͨ������
	char			addr[DEFAULT_DESCRIBE_LENG]; //��ͨ����ַ
}Item_MeetVideoDetailInfo, *pItem_MeetVideoDetailInfo;

//type:TYPE_MEET_INTERFACE_MEETVIDEO
//method: add/mod/del/query
typedef struct
{
	Type_HeaderInfo hdr;
	int32u	 num; //
}Type_MeetVideoDetailInfo, *pType_MeetVideoDetailInfo;

//����˫����ʾ��Ϣ
//type:TYPE_MEET_INTERFACE_MEETTABLECARD
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;
}Type_MeetTableCard, *pType_MeetTableCard;

//type
#define conf_mtgname	0x01 //��������
#define conf_memname	0x02 //�λ���Ա����
#define conf_job		0x03 //�λ���Աְҵ
#define conf_company	0x04 //�λ���Ա��λ
#define conf_position	0x05 //��λ����

//flag
#define MEET_TABLECARDFLAG_SHOW			0x00000001 //��λ���ڱ�ʾ�����Ƿ�ɼ�
#define MEET_TABLECARDFLAG_BOLD			0x00000002 //�Ӵ�
#define MEET_TABLECARDFLAG_LEAN			0x00000004 //��б
#define MEET_TABLECARDFLAG_UNDERLINE	0x00000008 //�»���

//����˫����ʾ���
typedef struct
{
	char fontname[DEFAULT_NAME_MAXLEN];//ʹ����������
	/* ���������С���㷽��
	fontsize * ��ǰ��Ļ�߶� / ��׼��Ļ�߶�
	eg: �յ������С��100����ǰ����Ļ�߶���720���������ʵ�������СΪ��100 * 720 / 1080 = 67
	*/
	int32u fontsize;//���� ��С
	int32u fontcolor;//������ɫ
	float lx;//���Ͻ� x���� ��Կ�İٷֱ� �磺lx=0.1 ����Ϊ1920ʱ ������lxӦ��= 0.1 * 1920 = 192
	float ly;
	float rx;
	float ry;
	int32u flag;
	int16u align;//�μ���ҳ font align flag
	int8u  type;//��ʾ����Ϣ
}Item_MeetTableCardDetailInfo, *pItem_MeetTableCardDetailInfo;

#define TABLECARD_MODFLAG_SETDEFAULT 0x00000001 //����ΪĬ��
//type:TYPE_MEET_INTERFACE_MEETTABLECARD
//method: mod/query
typedef struct
{
	Type_HeaderInfo hdr;
	int32u   modifyflag;//�޸ı�־
	int32u   bgphotoid;//��ͼID
	int32u	 num; //������3��
}Type_MeetTableCardDetailInfo, *pType_MeetTableCardDetailInfo;

//������λ��Ϣ
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;
	int32u opermethod;//����֪ͨ����Ϊopermethod����������
	int32u id;//���ָ����ID�ձ�ʾ�Ƕ��ض�ID�Ĳ���,Ϊ0��ʾȫ��

}Type_MeetSeat, *pType_MeetSeat;

//������λ��Ϣ
typedef struct
{
	int32u			nameId;//�λ���ԱID
	int32u			seatid;//�豸ID
	int				role;  //��Ա��� 

}Item_MeetSeatDetailInfo, *pItem_MeetSeatDetailInfo;

//type:TYPE_MEET_INTERFACE_MEETSEAT
//method: mod/query
typedef struct
{
	Type_HeaderInfo hdr;
	int32u	 num; //
}Type_MeetSeatDetailInfo, *pType_MeetSeatDetailInfo;

//��ѯ������λ������Ϣ
//property id
#define MEETSEAT_PROPERTY_SEATID			1 //����ԱID�����豸ID query
#define MEETSEAT_PROPERTY_MEMBERID			2 //���豸ID������ԱID query
#define MEETSEAT_PROPERTY_ROLEBYMEMBERID				3 //����ԱID������Աrole query
#define MEETSEAT_PROPERTY_ROLEBYDEVICEID				4 //����ԱID������Աrole query
#define MEETSEAT_PROPERTY_ISCOMPEREEBYMEMBERID		5 //����ԱID�жϵ�ǰ��Ա�ǲ��������� query
#define MEETSEAT_PROPERTY_ISCOMPEREEBYDEVICEID		6 //���豸ID�жϵ�ǰ��Ա�ǲ��������� query
#define MEETSEAT_PROPERTY_MEMBERIDBYROLE	7 //���ص���role����ԱID query
#define MEETSEAT_PROPERTY_DEVICEIDBYROLE	8 //���ص���role���豸ID query
#define MEETSEAT_PROPERTY_COMPEEMEMID	9 //���ص�ǰ���������˵���ԱID query
#define MEETSEAT_PROPERTY_COMPEEDEVID	10 //���ص�ǰ���������˵��豸ID query

//method: queryproperty
typedef struct
{
	Type_HeaderInfo hdr;

	int32u propertyid;//����ID
	int32u parameterval;//������� Ϊ0��ʾ��ȡ�����Ķ�Ӧ��Ϣ
	int32u propertyval;//ֵ 
	
}Type_MeetSeatQueryProperty, *pType_MeetSeatQueryProperty;

//ɨ��������
//type:TYPE_MEET_INTERFACE_MEMBER
//method: enter
typedef struct
{
	Type_HeaderInfo hdr;

	int32u meetingid;//ɨ�����Ļ���ID
	int32u memberrole;//�λ��˽�ɫ
	Item_MemberDetailInfo memberinfo;//�λ���Ա����Ϣ,����λ���ID��Ϊ0����ʾ�½�һ���λ���Ա

}Type_ScanEnterMeet, *pType_ScanEnterMeet;

//���齻����Ϣ
//type
#define MEETIM_CHAT_Message 0 //�ı���Ϣ
#define MEETIM_CHAT_Link 1	  //��ý������
#define MEETIM_CHAT_Water 2		//ˮ
#define MEETIM_CHAT_Tea 3		//��
#define MEETIM_CHAT_Coffee 4    //����
#define MEETIM_CHAT_Pen 5		//��
#define MEETIM_CHAT_Paper 6		//ֽ
#define MEETIM_CHAT_Technical 7//����Ա
#define MEETIM_CHAT_Waiter 8//����Ա
#define MEETIM_CHAT_Other 9		//��������
#define MEETIM_CHAT_Emecc 10	//��������

#define MEETIM_CHAR_MSG_MAXLEN 300
//callback
//type:TYPE_MEET_INTERFACE_MEETIM
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;

	int8u	    msgtype;//��Ϣ����
	int8u		role;//�����߽�ɫ
	int32u	    memberid;//������ID
	char		msg[MEETIM_CHAR_MSG_MAXLEN];//��Ϣ�ı�
	int64u	    utcsecond;//����UTCʱ��

	//��Ի����ˮ�����ṩ��
	char meetname[DEFAULT_DESCRIBE_LENG];//��������
	char roomname[DEFAULT_DESCRIBE_LENG];//��������
	char membername[DEFAULT_DESCRIBE_LENG];//��Ա����
	char seatename[DEFAULT_DESCRIBE_LENG];//ϯλ��

	int  membernum;
	int32u* pmemberid;
}Type_MeetIM, *pType_MeetIM;

//call
//type:TYPE_MEET_INTERFACE_MEETIM
//method: send
typedef struct
{
	Type_HeaderInfo hdr;

	int8u	    msgtype;//��Ϣ����
	char		msg[MEETIM_CHAR_MSG_MAXLEN];//��Ϣ�ı�
	int			usernum;//Ϊ0��ʾ��ǰ����ȫ�� ����ǻ������������ usernum=0
	//int32u      userids[];
}Type_SendMeetIM, *pType_SendMeetIM;

//���齻����Ϣ
typedef struct
{
	int8u	    msgtype;//��Ϣ����
	int8u		role;//�����߽�ɫ
	int32u	    memberid;//������ID
	char		msg[MEETIM_CHAR_MSG_MAXLEN];//��Ϣ�ı�
	int64u	    utcsecond;//����UTCʱ��
	int			usernum;//Ϊ0��ʾ��ǰ����ȫ�� ����ǻ������������ usernum=0
	//int32u      userids[];
}Item_MeetIMDetailInfo, *pItem_MeetIMDetailInfo;

//callreturn
//type:TYPE_MEET_INTERFACE_MEETIM
//method: query
typedef struct
{
	Type_HeaderInfo hdr;
	int32u	 num; //
}Type_MeetIMDetailInfo, *pType_MeetIMDetailInfo;

//queryflag ��Ҫ���Ǹ���ѯ��λ��Ϊ1
#define COMPLEXQUERY_MSGTYPE	0x0000001
#define COMPLEXQUERY_ROLE		0x0000002
#define COMPLEXQUERY_MEMBERID	0x0000004
#define COMPLEXQUERY_UTCTIME	0x0000008

//��ϲ�ѯ
//type:TYPE_MEET_INTERFACE_MEETIM
//method: complexquery ��pTypePageResQueryInfo ʹ�ð�ҳ��ѯ��ʽ���ؽ��)
typedef struct
{
	Type_HeaderInfo hdr;

	int32u      queryflag; //��ѯ��־

	int8u	    msgtype;//��Ϣ����
	int8u		role;//�����߽�ɫ
	int32u	    memberid;//������ID

	//ʱ�䷶Χ
	int64u	    startutcsecond;//����UTCʱ��
	int64u	    endutcsecond;//����UTCʱ��

	int			pageindex;//��ʼ���� ��0 ��ʼ
	int			pagenum;//ÿҳ����

}Type_MeetComplexQueryIM, *pType_MeetComplexQueryIM;

//ͶƱ״̬ votestate 
#define vote_notvote 0 //δ�����ͶƱ
#define vote_voteing 1 //���ڽ��е�ͶƱ
#define vote_endvote 2 //�Ѿ�������ͶƱ

//maintype //ͶƱ ѡ�� �����ʾ�
#define VOTE_MAINTYPE_vote			0 //ͶƱ
#define VOTE_MAINTYPE_election		1 //ѡ��
#define VOTE_MAINTYPE_questionnaire 2 //�ʾ����

//mode
#define VOTEMODE_agonymous	0 //����ͶƱ
#define VOTEMODE_signed		1 //����ͶƱ

//type
#define VOTE_TYPE_MANY			    0 //��ѡ
#define VOTE_TYPE_SINGLE			1 //��ѡ
#define VOTE_TYPE_4_5			    2 //5ѡ4
#define VOTE_TYPE_3_5			    3 //5ѡ2
#define VOTE_TYPE_2_5			    4 //5ѡ2
#define VOTE_TYPE_2_3			    5 //3ѡ2

//���鷢��ͶƱ��Ϣ
//type:TYPE_MEET_INTERFACE_MEETONVOTING
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;
	int32u opermethod;//����֪ͨ����Ϊopermethod����������
	int32u id;//���ָ����ID�ձ�ʾ�Ƕ��ض�ID�Ĳ���,Ϊ0��ʾȫ��

}Type_MeetOnVoting, *pType_MeetOnVoting;

//���鷢��ͶƱ��Ϣ
typedef struct
{
	int32u		voteid;
	char		content[DEFAULT_VOTECONTENT_LENG]; //ͶƱ���� 
	int			maintype; //ͶƱ ѡ�� �����ʾ�
	int			mode; //����ͶƱ ����ͶƱ
	int			type; //��ѡ ��ѡ
	int32u		timeouts;  //��ʱֵ
	int32u		selectcount; //��Чѡ��
	char		text[MEET_MAX_VOTENUM][DEFAULT_VOTEITEM_LENG];  //ѡ����������

	//����ͶƱ�Ĳ���,ֻ��ʹ�ò�ѯ����ʱ��Ч,��������Ҫ����
	int32u      voteflag; //����ͶƱ��־ �μ�MEET_VOTING_FLAG_NOPOST ����
	int			membernum;
	//int32u	memberids[];
}Item_MeetOnVotingDetailInfo, *pItem_MeetOnVotingDetailInfo;

//type:TYPE_MEET_INTERFACE_MEETONVOTING
//method: query/add/mod/
typedef struct
{
	Type_HeaderInfo hdr;
	int32u	 num; //
}Type_MeetOnVotingDetailInfo, *pType_MeetOnVotingDetailInfo;

//����ͶͶƱ��ʱֵ
//type:TYPE_MEET_INTERFACE_MEETONVOTING
//method: set
typedef struct
{
	Type_HeaderInfo hdr;
	int32u	 voteid; //ͶƱID
	int32u   timeouts;//��ʱ
}Type_MeetSetVoteTimeouts, *pType_MeetSetVoteTimeouts;

//���鷢��ͶƱ��Ϣ
//voteflag
#define MEET_VOTING_FLAG_NOPOST	   0x00000001 //����ͶӰ������ʾͶƱ���
#define MEET_VOTING_FLAG_SECRETARY 0x00000002 //ͶƱѡ���ͶƱģʽ
#define MEET_VOTING_FLAG_AUTOEXIT  0x00000004 //�Զ���ʱ���� --�ɷ��𷽼�ʱ���ͽ���
#define MEET_VOTING_FLAG_REVOTE    0x00000008 //��Ͷ ���֮ǰ�ļ�¼

typedef struct
{
	int32u memberid;
}SubItem_VoteMemFlag, *pSubItem_VoteMemFlag;

typedef struct
{
	int32u voteid;//�����ͶƱID
	int32u voteflag; //����ͶƱ��־ �μ�MEET_VOTING_FLAG_NOPOST ����
	int32u timeouts; //��ʱ���� ��λ����
	int32u membernum;//����ͶƱ�Ĳλ���Ա��
	//SubItem_VoteMemFlag members[];
}Item_VoteStartFlag, *pItem_VoteStartFlag;

//type:TYPE_MEET_INTERFACE_MEETONVOTING
//method: start
typedef struct
{
	Type_HeaderInfo hdr;
	int32u	 num; //
}Type_MeetStartVoteInfo, *pType_MeetStartVoteInfo;

//����ֹͣ��ɾ��ͶƱ��Ϣ
typedef struct
{
	int32u		voteid;
}Item_MeetStopVoteInfo, *pItem_MeetStopVoteInfo;

//type:TYPE_MEET_INTERFACE_MEETONVOTING
//method: stop/del
typedef struct
{
	Type_HeaderInfo hdr;
	int32u	 num; //
}Type_MeetStopVoteInfo, *pType_MeetStopVoteInfo;

//ͶƱ��־ selitem��8λ
#define VOTE_SELFLAG_MASK     0xff000000 //����
#define VOTE_SELFLAG_CHECKIN  0x80000000 //��λΪ1��ʾ�Ѿ�ǩ��

#define VOTE_CHECKIN_BIT_INDEX  31 //ǩ������λ

//�ύ����ͶƱ��Ϣ
typedef struct
{
	int32u		voteid;
	int			selcnt;//��Чѡ����
	int32u		selitem;//ѡ����� 0x00000001 ѡ���˵�һ�� 0x00000002�ڶ��� ��Ӧ��λ��1��ʾѡ�� | ��8λ����
}Item_MeetSubmitVote, *pItem_MeetSubmitVote;

//type:TYPE_MEET_INTERFACE_MEETONVOTING
//method: submit
typedef struct
{
	Type_HeaderInfo hdr;
	int32u	 num; //
}Type_MeetSubmitVote, *pType_MeetSubmitVote;

//����ͶƱ��Ϣ
//type:TYPE_MEET_INTERFACE_MEETVOTEINFO
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;
	int32u opermethod;//����֪ͨ����Ϊopermethod����������
	int32u id;//���ָ����ID�ձ�ʾ�Ƕ��ض�ID�Ĳ���,Ϊ0��ʾȫ��

}Type_MeetVoteInfo, *pType_MeetVoteInfo;

//����ͶƱ��Ϣ
typedef struct
{
	char    text[DEFAULT_VOTEITEM_LENG];//��������
	int32u  selcnt;	 //ͶƱ��
}SubItem_VoteItemInfo, *pSubItem_VoteItemInfo;

typedef struct
{
	int32u		voteid;
	char		content[DEFAULT_VOTECONTENT_LENG]; //ͶƱ���� 
	int			maintype;//��� ͶƱ ѡ�� �����ʾ�
	int			mode; //����ͶƱ ����ͶƱ
	int			type; //��ѡ ��ѡ
	int			votestate; //ͶƱ״̬
	int32u		timeouts;  //��ʱֵ
	int32u      selcnt; //ͶƱѡ�� 0x00000001 ѡ���˵�һ�� 0x00000002�ڶ��� ��Ӧ��λ��1��ʾѡ��
	int32u						selectcount; //��Чѡ��
	SubItem_VoteItemInfo		item[MEET_MAX_VOTENUM];  //ѡ����������

}Item_MeetVoteDetailInfo, *pItem_MeetVoteDetailInfo;

//type:TYPE_MEET_INTERFACE_MEETVOTEINFO
//method: query
typedef struct
{
	Type_HeaderInfo hdr;
	int32u	 num; //
}Type_MeetVoteDetailInfo, *pType_MeetVoteDetailInfo;

//type:TYPE_MEET_INTERFACE_MEETVOTEINFO
//method: complexquery
typedef struct
{
	Type_HeaderInfo hdr;
	int32u	 maintype; //ָ������ �μ����ļ��е�maintype ����
}Type_MeetVoteComplexQuery, *pType_MeetVoteComplexQuery;

//����ͶƱ��
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;
	int32u opermethod;//����֪ͨ����Ϊopermethod����������
	int32u id;//���ָ����ID�ձ�ʾ�Ƕ��ض�ID�Ĳ���,Ϊ0��ʾȫ��

}Type_MeetVoteSignedInfo, *pType_MeetVoteSignedInfo;

//����ͶƱ��
typedef struct
{
	int32u		 id;//ͶƱ��Ա
	int32u       selcnt; //ͶƱѡ�� 0x00000001 ѡ���˵�һ�� 0x00000002�ڶ��� ��Ӧ��λ��1��ʾѡ��
}Item_MeetVoteSignInDetailInfo, *pItem_MeetVoteSignInDetailInfo;

//type:TYPE_MEET_INTERFACE_MEETVOTESIGNED
//method: query
typedef struct
{
	Type_HeaderInfo hdr;
	int32u   voteid;
	int32u	 num; //
}Type_MeetVoteSignInDetailInfo, *pType_MeetVoteSignInDetailInfo;

//��ѯ����ͶƱ��������Ϣ
//property id
#define MEETVOTE_PROPERTY_HADSIGNIN			    1 //��ͶƱID\��ԱID�����Ƿ��Ѿ�ͶƱ query
#define MEETVOTE_PROPERTY_SIGNINITEM		    2 //��ͶƱID\��ԱID����ͶƱ���¼ //0x0001ѡ����ѡ��һ,0x0010ѡ����ѡ��� query
#define MEETVOTE_PROPERTY_HADCHECKINNUM		    3 //��ͶƱID\��ԱID�����Ƿ��Ѿ�ͶƱǩ�� query
#define MEETVOTE_PROPERTY_CHECKINNUM		    4 //��ͶƱID\����ͶƱʵ��ǩ��������  query
#define MEETVOTE_PROPERTY_ATTENDNUM		        5 //��ͶƱID\����ͶƱӦ��������  query
#define MEETVOTE_PROPERTY_VOTEDNUM		        6 //��ͶƱID\����ͶƱ�Ѿ�ͶƱ������  query

//type:TYPE_MEET_INTERFACE_MEETVOTESIGNED
//method: queryproperty
typedef struct
{
	Type_HeaderInfo hdr;

	int32u propertyid;//����ID
	int32u voteid;//�������
	int32u memberid;//������� Ϊ0��ʾ����
	int32u propertyval;//����ֵ 

}Type_MeetVoteQueryProperty, *pType_MeetVoteQueryProperty;

//����ǩ��
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;
	int32u opermethod;//����֪ͨ����Ϊopermethod����������
	int32u id;//���ָ����ID�ձ�ʾ�Ƕ��ض�ID�Ĳ���,Ϊ0��ʾȫ��

}Type_MeetSignin, *pType_MeetSignin;

//����ǩ��
typedef struct
{
	int32u		nameId; //��ԱID
	int64u		utcseconds;  //���� ʱ�� ��λ:��
	int8u		signin_type;//ǩ����ʽ

	int			signdatalen;
	//char		psigndata[signdatalen]; //ǩ������ ���������ǩ����Ϊǩ������,ͼƬǩ����ΪpngͼƬ����

}Item_MeetSignInDetailInfo, *pItem_MeetSignInDetailInfo;

//type:TYPE_MEET_INTERFACE_MEETVOTESIGNED
//method: query/add
typedef struct
{
	Type_HeaderInfo hdr;
	int32u	 num; //
}Type_MeetSignInDetailInfo, *pType_MeetSignInDetailInfo;

//��ѯ����ǩ��������Ϣ
//property id
#define MEETSIGN_PROPERTY_HADSIGNIN			    1 //����ԱID�����Ƿ��Ѿ�ǩ�� query
#define MEETSIGN_PROPERTY_SIGNINTYPE			2 //����ԱID����ǩ������ query

//type:TYPE_MEET_INTERFACE_MEETSIGN
//method: queryproperty
typedef struct
{
	Type_HeaderInfo hdr;

	int32u propertyid;//����ID
	int32u parameterval;//������� Ϊ0��ʾ��ȡ�����Ķ�Ӧ��Աǩ����Ϣ
	int32u propertyval;//ֵ 

}Type_MeetSignInQueryProperty, *pType_MeetSignInQueryProperty;

//���ͻ���ǩ��
//type:TYPE_MEET_INTERFACE_MEETSIGN
//method: add
typedef struct
{
	Type_HeaderInfo hdr;
	
	int32u      memberid;//ָ��ǩ������ԱID,Ϊ0��ʾ��ǰ�󶨵���Ա

	int8u		signin_type;//ǩ����ʽ

	char		password[DEFAULT_PASSWORD_LENG];//ǩ������
	int			signdatalen;//ͼƬǩ�����ݴ�С
	//char		psigndata[signdatalen]; //ǩ������ ���������ǩ����Ϊǩ������,ͼƬǩ����ΪpngͼƬ����
}Type_DoMeetSignIno, *pType_DoMeetSignIno;

//ɾ������ǩ��
//type:TYPE_MEET_INTERFACE_MEETSIGN
//method: del
typedef struct
{
	Type_HeaderInfo hdr;

	int32u      meetingid;//ָ������ID 0��ʾ�󶨵Ļ���

	int			membernum;//Ϊ0��ʾɾ��ָ�������ȫ����Աǩ��
	//int32u    memberid[];
}Type_DoDeleteMeetSignIno, *pType_DoDeleteMeetSignIno;

//����װ����

//operflag
#define MEETPOTIL_FLAG_FORCEOPEN	   1 //�ñ�־��ʾ��Ҫǿ��ִ����ע�װ�ǿ�д�
#define MEETPOTIL_FLAG_FORCECLOSE	   2 //�ñ�־��ʾ��Ҫǿ��ִ����ע�װ�ǿ�йر�
#define MEETPOTIL_FLAG_REQUESTOPEN	   3 //�ñ�־��ʾ���˷���װ�
#define MEETPOTIL_FLAG_REJECTOPEN	   4 //�ñ�־��ʾ�Է��ܾ��򿪰װ�
#define MEETPOTIL_FLAG_EXIT			   5 //�ñ�־��ʾ�����뿪�װ�
#define MEETPOTIL_FLAG_ENTER		   6 //�ñ�־��ʾ���˽���װ�

//ִ��һ���װ���� ���ڻ��齻�� ָ����Ҫִ�в����ı�־
//call
//type:TYPE_MEET_INTERFACE_WHITEBOARD\TYPE_MEET_INTERFACE_PDFWHITEBOARD
//method: control 
typedef struct
{
	Type_HeaderInfo hdr;

	int32u  operflag;//ָ��������־
	char    medianame[DEFAULT_DESCRIBE_LENG];	//�װ��������
	int32u  opermemberid;//��ǰ���������ԱID
	int32u  srcmemid;//�����˵���ԱID �װ��ʶʹ��
	int64u  srcwbid;//�����˵İװ��ʶ ȡ΢�뼶��ʱ������ʶ �װ��ʶʹ��
	//for pdf
	int32u  fileid;
	int32u  pageindex;
	int		usernum;//�λ���Ա����
	//int32u userid[usernum];
}Type_MeetWhiteBoardControl, *pType_MeetWhiteBoardControl;

//callback
//type:TYPE_MEET_INTERFACE_WHITEBOARD\TYPE_MEET_INTERFACE_PDFWHITEBOARD
//method: ask �ص���������1��ʾͬ����룬����ֵ��ʾ�ܾ�����
typedef struct
{
	Type_HeaderInfo hdr;

	int32u  operflag;//���ݸ�ֵ�ж�
	char    medianame[DEFAULT_DESCRIBE_LENG];	//�װ�����
	int32u  opermemberid;//��ǰ���������ԱID
	int32u  srcmemid;//�����˵���ԱID �װ��ʶʹ��
	int64u  srcwbid;//�����˵İװ��ʶ ȡ΢�뼶��ʱ������ʶ �װ��ʶʹ��

	//for pdf
	int32u  fileid;
	int32u  pageindex;
}Type_MeetStartWhiteBoard, *pType_MeetStartWhiteBoard;

//�������˾ܾ������㷢��İװ�ص�
//callback/call
//type:TYPE_MEET_INTERFACE_WHITEBOARD\TYPE_MEET_INTERFACE_PDFWHITEBOARD
//method: reject
typedef struct
{
	Type_HeaderInfo hdr;

	int32u  opermemberid;//��ǰ���������ԱID
	int32u  srcmemid;//�����˵���ԱID �װ��ʶʹ��
	int64u  srcwbid;//�����˵İװ��ʶ ȡ΢�뼶��ʱ������ʶ �װ��ʶʹ��

}Type_MeetRejectWhiteBoard, *pType_MeetRejectWhiteBoard;

//���������˳����㷢��İװ�ص�
//callback
//type:TYPE_MEET_INTERFACE_WHITEBOARD\TYPE_MEET_INTERFACE_PDFWHITEBOARD
//method: exit
typedef struct
{
	Type_HeaderInfo hdr;

	int32u  opermemberid;//��ǰ���������ԱID
	int32u  srcmemid;//�����˵���ԱID �װ��ʶʹ��
	int64u  srcwbid;//�����˵İװ��ʶ ȡ΢�뼶��ʱ������ʶ �װ��ʶʹ��

}Type_MeetExitWhiteBoard, *pType_MeetExitWhiteBoard;

//�������˼������㷢��İװ�ص�
//callback/call
//type:TYPE_MEET_INTERFACE_WHITEBOARD\TYPE_MEET_INTERFACE_PDFWHITEBOARD
//method: enter
typedef struct
{
	Type_HeaderInfo hdr;

	int32u  opermemberid;//��ǰ���������ԱID
	int32u  srcmemid;//�����˵���ԱID �װ��ʶʹ��
	int64u  srcwbid;//�����˵İװ��ʶ ȡ΢�뼶��ʱ������ʶ �װ��ʶʹ��

}Type_MeetEnterWhiteBoard, *pType_MeetEnterWhiteBoard;

//�װ��������
#define  WB_FIGURETYPE_INK			1 //����
#define  WB_FIGURETYPE_LINE		2 //ֱ��
#define  WB_FIGURETYPE_ELLIPSE		3 //��Բ
#define  WB_FIGURETYPE_RECTANGLE	4 //����
#define  WB_FIGURETYPE_FREETEXT	5 //����
#define  WB_FIGURETYPE_PICTURE		6 //
#define  WB_FIGURETYPE_ARROW		7 //��ͷ

//callback
//method: clear, del
typedef struct
{
	Type_HeaderInfo hdr;

	int32u  operid;	    //����ID �ն˼������ Ϊ0��ʾ������е���opermemberid�İװ����
	int32u  opermemberid;//��ǰ���������ԱID Ϊ0��ʾ������е���opermemberid�İװ����
	int32u  srcmemid;//�����˵���ԱID �װ��ʶʹ��
	int64u  srcwbid;//�����˵İװ��ʶ ȡ΢�뼶��ʱ������ʶ �װ��ʶʹ��

	int64u  utcstamp;	//ʱ���
	int     figuretype;	//ͼ������ Ϊ0��ʾ������е���figuretype�İװ����
}Type_MeetClearWhiteBoard, *pType_MeetClearWhiteBoard;

//(call)
//method: delall, del 
typedef struct
{
	Type_HeaderInfo hdr;

	int32u  memberid;   //��Ҫɾ������ԱID Ϊ0��ʾ������е�����Ա�İװ����
	int32u  operid;	    //����ID �ն˼������ Ϊ0��ʾ������е���opermemberid�İװ����

	int32u  opermemberid;//��ǰ���������ԱID
	int32u  srcmemid;//�����˵���ԱID �װ��ʶʹ��
	int64u  srcwbid;//�����˵İװ��ʶ ȡ΢�뼶��ʱ������ʶ �װ��ʶʹ��

	int64u  utcstamp;	//ʱ���
	int     figuretype;	//ͼ������ Ϊ0��ʾ����������͵İװ����
}Type_MeetDoClearWhiteBoard, *pType_MeetDoClearWhiteBoard;

//�װ�����ͨ��ѯ
//(call)
//method: query
typedef struct
{
	Type_HeaderInfo hdr;

	int32u  srcmemid;//�����˵���ԱID �װ��ʶʹ��
	int64u  srcwbid;//�����˵İװ��ʶ ȡ΢�뼶��ʱ������ʶ �װ��ʶʹ��
	int     figuretype;	//ͼ������ ����ָ��һ������,��Ϊ��ͬ���ͽṹ��ͬ,���ý���,����Ҫ�����Ͳ�ѯ

}Type_MeetWhiteBoardQuery, *pType_MeetWhiteBoardQuery;

//(call)
//method: complexquery
//queryflag ָ����Ч�Ĳ�ѯ��־
typedef struct
{
	Type_HeaderInfo hdr;

	int32u  srcmemid;//�����˵���ԱID �װ��ʶʹ��
	int64u  srcwbid;//�����˵İװ��ʶ ȡ΢�뼶��ʱ������ʶ �װ��ʶʹ��
	int     figuretype;	//ͼ������ //ͼ������ ����ָ��һ������,��Ϊ��ͬ���ͽṹ��ͬ,���ý���,����Ҫ�����Ͳ�ѯ

	int32u  opermemberid;//��ԱID

}Type_MeetWhiteBoardComplexQuery, *pType_MeetWhiteBoardComplexQuery;

//ink
//callback\call
//type:TYPE_MEET_INTERFACE_WHITEBOARD\TYPE_MEET_INTERFACE_PDFWHITEBOARD
//method: add
typedef struct
{
	Type_HeaderInfo hdr;

	int32u operid;	    //����ID �ն˼������
	int32u  opermemberid;//��ǰ���������ԱID
	int32u  srcmemid;//�����˵���ԱID �װ��ʶʹ��
	int64u  srcwbid;//�����˵İװ��ʶ ȡ΢�뼶��ʱ������ʶ �װ��ʶʹ��
	int64u utcstamp;	//ʱ���
	int    figuretype;	//ͼ������

	int8u  linesize;			//�������
	int32u Argb;			//������ɫ

	//for pdf
	int32u  fileid;
	int32u  pageindex;

	int32u ptnum;//ink point Num, float[2 * ptnum](x,y��������)
	float* pinklist;//ע�����ΪPDF������д�������Ϊ�������
	
}Type_MeetWhiteBoardInkItem, *pType_MeetWhiteBoardInkItem;

//��ѯInk�װ����
//callreturn
//method: query pagequery
typedef struct
{
	int32u  operid;	    //����ID �ն˼������
	int32u  opermemberid;//��ǰ���������ԱID
	int64u  utcstamp;	//ʱ���

	int8u  linesize;			//�������
	int32u Argb;			//������ɫ

	int32u ptnum;//ink point Num, float[2 * ptnum](x,y��������)
	//float pinklist[2 * ptnum];

}Item_MeetWBInkDetail, *pItem_MeetWBInkDetail;

typedef struct
{
	Type_HeaderInfo hdr;

	int32u num;	    //
}Type_MeetWBInkDetail, *pType_MeetWBInkDetail;

//rect line elipse
//callback\call
//method: add
typedef struct
{
	Type_HeaderInfo hdr;

	int32u operid;	    //����ID �ն˼������
	int32u  opermemberid;//��ǰ���������ԱID
	int32u  srcmemid;//�����˵���ԱID �װ��ʶʹ��
	int64u  srcwbid;//�����˵İװ��ʶ ȡ΢�뼶��ʱ������ʶ �װ��ʶʹ��
	int64u utcstamp;	//ʱ���
	int    figuretype;	//ͼ������

	int8u  linesize;			//�������
	int32u Argb;			//������ɫ
	float  pt[4];		    //(lx,ly,rx,ry ���Ͻ�,���½�����)

}Type_MeetWhiteBoardRectItem, *pType_MeetWhiteBoardRectItem;

//callreturn
//method: query pagequery
typedef struct
{
	int32u  operid;	    //����ID �ն˼������
	int32u  opermemberid;//��ǰ���������ԱID
	int32u  srcmemid;//�����˵���ԱID �װ��ʶʹ��
	int64u  srcwbid;//�����˵İװ��ʶ ȡ΢�뼶��ʱ������ʶ �װ��ʶʹ��
	int64u  utcstamp;	//ʱ���
	int     figuretype;	//ͼ������

	int8u   linesize;			//�������
	int32u  Argb;			//������ɫ
	float   pt[4];		    //(lx,ly,rx,ry ���Ͻ�,���½�����)

}Item_MeetWBRectDetail, *pItem_MeetWBRectDetail;

typedef struct
{
	Type_HeaderInfo hdr;

	int32u num;	    //
}Type_MeetWBRectDetail, *pType_MeetWBRectDetail;


//font flag
#define WHITEBOARD_FONT_STRIKEOUT 0x01 //ɾ����
#define WHITEBOARD_FONT_BOLD	  0x02 //�Ӵ�
#define WHITEBOARD_FONT_ITALIC	  0x04 //��б
#define WHITEBOARD_FONT_UNDERLINE 0x08 //�»���

#define FONTNAME_LENG	48  //������󳤶ȣ������ַ�����������������Ա���֣��豸����
//callback\call
//method: add
typedef struct
{
	Type_HeaderInfo hdr;

	int32u operid;	    //����ID �ն˼������
	int32u  opermemberid;//��ǰ���������ԱID
	int32u  srcmemid;//�����˵���ԱID �װ��ʶʹ��
	int64u  srcwbid;//�����˵İװ��ʶ ȡ΢�뼶��ʱ������ʶ �װ��ʶʹ��
	int64u utcstamp;	//ʱ���
	int    figuretype;	//ͼ������

	int8u  fontsize;			//�����С
	int8u  fontflag;			//������������
	int32u Argb;				//������ɫ
	char   fontname[FONTNAME_LENG]; //��������
	float  pos[2];				//(lx,ly,���Ͻ�����)

	int32u textlen;			    //char[](����Ϊ textlen)
	char*  ptext;
}Type_MeetWhiteBoardTextItem, *pType_MeetWhiteBoardTextItem;

//callreturn
//method: query pagequery
typedef struct
{
	int32u operid;	    //����ID �ն˼������
	int32u  opermemberid;//��ǰ���������ԱID
	int32u  srcmemid;//�����˵���ԱID �װ��ʶʹ��
	int64u  srcwbid;//�����˵İװ��ʶ ȡ΢�뼶��ʱ������ʶ �װ��ʶʹ��
	int64u  utcstamp;	//ʱ���
	int     figuretype;	//ͼ������

	int8u  fontsize;			//�����С
	int8u  fontflag;			//������������
	int32u Argb;				//������ɫ
	char   fontname[FONTNAME_LENG]; //��������
	float  pos[2];				//(lx,ly,���Ͻ�����)

	int32u textlen;			    //char[](����Ϊ textlen)
	//char  ptext[textlen];

}Item_MeetWBTextDetail, *pItem_MeetWBTextDetail;

typedef struct
{
	Type_HeaderInfo hdr;

	int32u num;	    //
}Type_MeetWBTextDetail, *pType_MeetWBTextDetail;

//callback call
//method: add
typedef struct
{
	Type_HeaderInfo hdr;

	int32u operid;	    //����ID �ն˼������
	int32u  opermemberid;//��ǰ���������ԱID
	int32u  srcmemid;//�����˵���ԱID �װ��ʶʹ��
	int64u  srcwbid;//�����˵İװ��ʶ ȡ΢�뼶��ʱ������ʶ �װ��ʶʹ��
	int64u utcstamp;	//ʱ���
	int    figuretype;	//ͼ������

	float  pos[2];				//(lx,ly,���Ͻ�����)

	int32u picsize;			    //char[](����Ϊ picsize)
	char*  picdata;
}Type_MeetWhiteBoardPicItem, *pType_MeetWhiteBoardPicItem;

//call return
//method: query pagequery
typedef struct
{
	int32u operid;	    //����ID �ն˼������
	int32u  opermemberid;//��ǰ���������ԱID
	int32u  srcmemid;//�����˵���ԱID �װ��ʶʹ��
	int64u  srcwbid;//�����˵İװ��ʶ ȡ΢�뼶��ʱ������ʶ �װ��ʶʹ��
	int64u utcstamp;	//ʱ���
	int    figuretype;	//ͼ������

	float  pos[2];				//(lx,ly,���Ͻ�����)

	int32u picsize;			    //char[](����Ϊ picsize)
	//char  picdata[picsize];

}Item_MeetWBPictureDetail, *pItem_MeetWBPictureDetail;

typedef struct
{
	Type_HeaderInfo hdr;

	int32u num;	    //
}Type_MeetWBPictureDetail, *pType_MeetWBPictureDetail;

//���鹦��
//type:TYPE_MEET_INTERFACE_FUNCONFIG
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;
	int32u opermethod;//����֪ͨ����Ϊopermethod����������
	int32u id;//���ָ����ID�ձ�ʾ�Ƕ��ض�ID�Ĳ���,Ϊ0��ʾȫ��

}Type_MeetFunConfig, *pType_MeetFunConfig;

//���ܱ�ʶ��
#define MEET_FUNCODE_AGENDA_BULLETIN	0	//�������
#define MEET_FUNCODE_MATERIAL		1	//��������
#define MEET_FUNCODE_SHAREDFILE		2	//	�����ļ�
#define MEET_FUNCODE_POSTIL			3	//��ע�ļ�
#define MEET_FUNCODE_MESSAGE			4	//���齻��
#define MEET_FUNCODE_VIDEOSTREAM		5	//��Ƶֱ��
#define MEET_FUNCODE_WHITEBOARD		6	//�װ�
#define MEET_FUNCODE_WEBBROWSER		7	//��ҳ
#define MEET_FUNCODE_VOTERESULT		8	//ͶƱ
#define MEET_FUNCODE_SIGNINRESULT	9	//ǩ��
#define MEET_FUNCODE_DOCUMENT		10	//�ⲿ�ĵ�

// ����ϵͳ��׼�� ���ӵĹ���ʶ����
#define MEET_FUNCODE_DATAREVIEW     11  // ��������
#define MEET_FUNCODE_MEETSERVICE    12  // �������
#define MEET_FUNCODE_MEETNOTE       13  // ����ʼ�
#define MEET_FUNCODE_WINDESK        14  // �������
#define MEET_FUNCODE_OTHERFUNC      15  // ��������

#define MEET_FUNCODE_QUESTIONNAIRE  30  // �ʾ����
#define MEET_FUNCODE_SCORE          31  // �ļ�����

//���鹦��
typedef struct
{
	int32u	funcode;	//���ܱ�ʶ��
	int32u	position;	//λ�ñ�ʶ��

}Item_MeetFunConfigDetailInfo, *pItem_MeetFunConfigDetailInfo;

#define FUNCONFIG_MODFLAG_SETDEFAULT 0x00000001 //����ΪĬ��
//type:TYPE_MEET_INTERFACE_FUNCONFIG
//method: query/save/
typedef struct
{
	Type_HeaderInfo hdr;
	int32u   modifyflag;//�޸ı�־
	int32u	 num; //
}Type_MeetFunConfigDetailInfo, *pType_MeetFunConfigDetailInfo;

//�λ��˰װ���ɫ
//callback
//type:TYPE_MEET_INTERFACE_MEMBERCOLOR
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;
	int32u opermethod;//����֪ͨ����Ϊopermethod����������
	int32u id;//���ָ����ID�ձ�ʾ�Ƕ��ض�ID�Ĳ���,Ϊ0��ʾȫ��

}Type_MeetMemberColor, *pType_MeetMemberColor;

//�λ��˰װ���ɫ
typedef struct
{
	int32u memberid;//�λ���ԱID
	int32u rgb;//��ɫֵ

}Item_MeetMemberColorDetailInfo, *pItem_MeetMemberColorDetailInfo;

//call
//type:TYPE_MEET_INTERFACE_MEMBERCOLOR
//method: query/mod/
typedef struct
{
	Type_HeaderInfo hdr;
	int32u	 num; //
}Type_MeetMemberColorDetailInfo, *pType_MeetMemberColorDetailInfo;

//��Ļ������
//callback
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;

	int32u  flag;			//���Ƶı�־  �ο�streamcontrol.h ����
	int32u  deviceid;       //����������豸ID
	int32u  memberid;       //�����������ԱID
	int32u  otherflag;     //�ο�streamcontrol.h ����
	float   x; //x% width�İٷ�
	float   y; //y% height�İٷ�
}Type_MeetScreenMouseControl, *pType_MeetScreenMouseControl;

//��Ļ������
//call
//method: control
typedef struct
{
	Type_HeaderInfo hdr;

	int32u  flag;			//���Ƶı�־  �ο�streamcontrol.h ����
	int32u  deviceid;       //�豸ID
	int32u  otherflag;     //�ο�streamcontrol.h ����
	float   x; //x / width�İٷֱ�
	float   y; //y / height�İٷֱ�
}Type_MeetDoScreenMouseControl, *pType_MeetDoScreenMouseControl;

//��Ļ���̿���
//callback
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;

	int32u  flag;			//���Ƶı�־ �ο�streamcontrol.h ����
	int32u  deviceid;       //����������豸ID
	int32u  memberid;       //�����������ԱID

	int32u  otherflag; //�ο�streamcontrol.h ����
	int32u  key; //��Ӧ��keyֵ �ο�streamcontrol.h ����
}Type_MeetScreenKeyBoardControl, *pType_MeetScreenKeyBoardControl;

//��Ļ���̿���
//call
//method: control
typedef struct
{
	Type_HeaderInfo hdr;

	int32u  flag;			//���Ƶı�־ �ο�streamcontrol.h ����
	int32u  deviceid;       //�豸ID

	int32u  otherflag; //�ο�streamcontrol.h ����
	int32u  key; //��Ӧ��keyֵ �ο�streamcontrol.h ����
}Type_MeetDoScreenKeyBoardControl, *pType_MeetDoScreenKeyBoardControl;

//ý�岥��
//playflag
#define MEDIA_PLAYFLAG_LOOP			0x00000001 //ѭ�����ű�־
#define MEDIA_PLAYFLAG_CLIENT		0x00000002 //�ն��豸 ��devnum=0ʱ���ж�
#define MEDIA_PLAYFLAG_PROJECTIVE   0x00000004 //ͶӰ�豸 ��devnum=0ʱ���ж�
#define MEDIA_PLAYFLAG_NOSELFID     0x00000008 //�ų��Լ����豸ID ��devnum=0ʱ���ж�
#define MEDIA_PLAYFLAG_SETPOSMODE   0x00000010 //����λ�õ��� 0�ٷֱ� Ϊ1��ʾ��ʱ��������룩

//ֹͣ��Դ����
//callback
//type:TYPE_MEET_INTERFACE_STOPPLAY
//method: close
typedef struct
{
	Type_HeaderInfo hdr;

	int   avalableres;//��Ч��Դ����,Ϊ0��ʾȫ��
	int8u res[32];
}Type_MeetStopResWork, *pType_MeetStopResWork;

//�ر���Դ����
//call
//type:TYPE_MEET_INTERFACE_STOPPLAY
//method: close
typedef struct
{
	Type_HeaderInfo hdr;

	int32u  playflag;//���ű�־ MEDIA_PLAYFLAG_CLIENT
	int32u  triggeruserval;//�μ����ļ��е�triggeruserval����

	int   avalableres;//��Ч��Դ����,Ϊ0��ʾȫ��
	int8u res[32];

	int   devnum;
	//int32u deviceid[devnum];
}Type_MeetDoStopResWork, *pType_MeetDoStopResWork;

//ֹͣ������
//call
//type:TYPE_MEET_INTERFACE_STOPPLAY
//method: stop
typedef struct
{
	Type_HeaderInfo hdr;

	int32u  playflag;//���ű�־ MEDIA_PLAYFLAG_CLIENT
	int32u  triggeruserval;//�μ����ļ��е�triggeruserval����
	int32u  triggerid;//������ID

	int   devnum;
	//int32u deviceid[devnum];
}Type_MeetDoStopTrrigerWork, *pType_MeetDoStopTrrigerWork;

//���ݺ�̨�ظ��Ĵ�����Ϣ
//callback
//type:TYPE_MEET_INTERFACE_DBSERVERERROR
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;
	int32u  type;
	int32u  method;
	int32u  status; 
}Type_MeetDBServerOperError, *pType_MeetDBServerOperError;

//��Ƶ������Դ��ʼ��
//call
//type:TYPE_MEET_INTERFACE_MEDIAPLAY
//method: init/resinfo(��ѯ������Դ��ʵ����ʾ����)/set(���ô��ڴ�С����Ļλ��)
typedef struct
{
	Type_HeaderInfo hdr;

	int8u res;//��Դ����
	void* parent;					//���ھ�� -1=��ʾʹ��SDL�ڲ���������
	int x, y, w, h;					//��Դ������Ϳ��
}Type_MeetInitPlayRes, *pType_MeetInitPlayRes;

//��Ƶ��Դ���Ŵ��ڵ���ʾ���ƣ�void* parent=-1�ǣ���SDL�ڲ������Ĵ��ڲ�ʹ�ã�
//ע����һЩ�豸���غ��޷���ʾ�����ô�С�Ĺ��ܣ�ͨ�����ͷŲ�����Դ�����³�ʼ��������Դʵ����������ʾ�Ĺ���
//call
//type:TYPE_MEET_INTERFACE_MEDIAPLAY
//method: control
typedef struct
{
	Type_HeaderInfo hdr;

	int8u res;//��Դ����
	int   braise; //0=no oper, 1=do raise window
	int   bshow; //0=hide, 1=show windows
	int   topx;//��Ļ���Ͻ�x���� =-1��ʾ���޸�
	int   topy;//��Ļ���Ͻ�y����  =-1��ʾ���޸�
}Type_MeetResWinControl, *pType_MeetResWinControl;

//��Ƶ������Դ�ͷ�
//call
//type:TYPE_MEET_INTERFACE_MEDIAPLAY
//method: destroy
typedef struct
{
	Type_HeaderInfo hdr;

	int8u res;//��Դ����
}Type_MeetDestroyPlayRes, *pType_MeetDestroyPlayRes;

//ý�岥��֪ͨ
//callback
//type:TYPE_MEET_INTERFACE_MEDIAPLAY
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;
	int32u  triggerid;//������ID
	int32u  createdeviceid;//����ִ�иô��������豸ID
	int32u  mediaid;//���ŵ�ý��id
	int8u   res;//�������õ���ԴID
	int32u  triggeruserval;//�μ����ļ��е�triggeruserval����
}Type_MeetMediaPlay, *pType_MeetMediaPlay;

//call
//type:TYPE_MEET_INTERFACE_MEDIAPLAY
//method: start
typedef struct
{
	Type_HeaderInfo hdr;

	int32u  playflag;//���ű�־

	int     pos;//ָ����ʼ���ŵİٷֱ�λ��
	int32u  mediaid;//ý��ID
	int32u  triggeruserval;//��������־

	int	   resnum;
	int8u  res[MAX_RES_NUM];
	int    devnum; //�����豸
	//int32u deviceid[devnum];
}Type_MeetDoMediaPlay, *pType_MeetDoMediaPlay;

//���ò���λ��
//call
//type:TYPE_MEET_INTERFACE_MEDIAPLAY
//method: move
typedef struct
{
	Type_HeaderInfo hdr;

	int8u   resindex;//��Դ������
	int     pos;//ָ����ʼ���ŵİٷֱ�λ��
	int32u  playflag;//���ű�־ MEDIA_PLAYFLAG_CLIENT
	int32u  triggeruserval;//�μ����ļ��е�triggeruserval����

	int    devnum; //�����豸
	//int32u deviceid[devnum];
}Type_MeetDoSetPlayPos, *pType_MeetDoSetPlayPos;

//���ò�����ͣ �ָ�
//call
//type:TYPE_MEET_INTERFACE_MEDIAPLAY
//method: pause play
typedef struct
{
	Type_HeaderInfo hdr;

	int8u   resindex;//��Դ������
	int32u  playflag;//���ű�־ MEDIA_PLAYFLAG_CLIENT
	int32u  triggeruserval;//�μ����ļ��е�triggeruserval����

	int     devnum; //�����豸
	//int32u deviceid[devnum];
}Type_MeetDoPlayControl, *pType_MeetDoPlayControl;

//��ѯý�岥��
typedef struct
{
	int32u trrigerid;//������ID
	int32u mediaid;//ý��ID
}Item_MeetMediaPlayDetailInfo, *pItem_MeetMediaPlayDetailInfo;

//type:TYPE_MEET_INTERFACE_MEDIAPLAY
//method: query
typedef struct
{
	Type_HeaderInfo hdr;
	int32u	 num; //
}Type_MeetMediaPlayDetailInfo, *pType_MeetMediaPlayDetailInfo;

//�ļ�����
//type:TYPE_MEET_INTERFACE_MEDIAPLAY
//method: notify
//��Ϊѯ��ģʽʱ���ص���������1��ʾͬ�ⲥ��
typedef struct
{
	Type_HeaderInfo hdr;
	int32u mediaid;
	int32u triggeruserval;//�μ����ļ��е�triggeruserval����
}Type_FilePush, *pType_FilePush;

//�ļ�����
//type:TYPE_MEET_INTERFACE_MEDIAPLAY
//method: push
typedef struct
{
	Type_HeaderInfo hdr;

	int32u mediaid;
	int32u triggeruserval;//�μ����ļ��е�triggeruserval����

	int	   devnum;
	//int32u devid[devnum];
}Type_DoFilePush, *pType_DoFilePush;


#define REQUEST_USERVAL_JOINPLAY	1//userdefval1 ���������ͬ�� ���ŵ�ǰ���ͷ�����ļ�
#define REQUEST_USERVAL_UPDATEPLAY	2//userdefval1 ���沥�ŵ�ǰ�ļ� ��Ҫmediaid==�������͵�meidaid

//����Ƶ�ļ����沥��
//type:TYPE_MEET_INTERFACE_MEDIAPLAY
//method: METHOD_MEET_INTERFACE_UPDATE
//��Ϊѯ��ģʽʱ���ص���������1��ʾͬ�ⲥ��
typedef struct
{
	Type_HeaderInfo hdr;

	int32u  mediaid;
	int32u  deviceid;//����������豸ID
	int8u   resindex;//��Դ������
	int8u   fill[3];
	int32u  triggeruserval;//�μ����ļ��е�triggeruserval����
	int32u  userdefval1;//�û��Զ����ֵ
}Type_ReqMediaUpdatePlay, *pType_ReqMediaUpdatePlay;

//�豸��λ
//call
//type:TYPE_MEET_INTERFACE_DEVICEOPER
//method:locate
typedef struct
{
	Type_HeaderInfo hdr;
	int		devnum;
	//int32u  deviceid[devnum];       //�豸ID
}Type_DoDeviceLocate, *pType_DoDeviceLocate;

//�豸��λ
//callback
//type:TYPE_MEET_INTERFACE_DEVICEOPER
//method: locate
typedef struct
{
	Type_HeaderInfo hdr;
	int32u  oeprdeviceid;
}Type_DeviceLocate, *pType_DeviceLocate;

/*�豸�Խ�����������
ǿ��ģʽ:A��Bǿ�Ʒ���������Ƶ�Խ���ִ�в��ŵĲ�����A��
ѯ��ģʽ:A��B����������Ƶ�Խ�ѯ�ʣ�B�˻�Ӧ��A,A����B��Ӧ�ı�־ִ�в��Ų���
*/

//inviteflag �豸�Խ���־��Ĭ���ǽ���һ��һ�Խ���
#define DEVICE_INVITECHAT_FLAG_SIMPLEX  0x00000001 //�Ƿ�Ϊ����Ѱ�� 1��ʾ����Ѱ��ʽ�Խ������������Խ���һ�Զࣩ 0˫��Խ� ����������ã�
#define DEVICE_INVITECHAT_FLAG_ASK		0x00000002 //ѯ��ʽ������жԽ� 1��ʾ�ȴ��Է�ͬ�� 0ǿ��ִ�� ����������ã�
#define DEVICE_INVITECHAT_FLAG_VIDEO	0x00000004 //�Ƿ������Ƶ ���������ã�
#define DEVICE_INVITECHAT_FLAG_AUDIO	0x00000008 //�Ƿ������Ƶ���������ã�
#define DEVICE_INVITECHAT_FLAG_DEAL 	0x00000010 //�Ƿ�ͬ�� �����ն����ã�
//�����豸�Խ�
//call
//type:TYPE_MEET_INTERFACE_DEVICEOPER
//method:requestinvite
typedef struct
{
	Type_HeaderInfo hdr;
	int32u  encodemode;//��������  DEVICE_INVITECHAT_ENCODEMODE_HIGH
	int32u  inviteflag;//�豸�Խ���־ �μ�DEVICE_INVITECHAT_FLAG_SIMPLEX
	int		devnum;
	//int32u  deviceid[devnum];       //�豸ID
}Type_DoDeviceChat, *pType_DoDeviceChat;

//�յ��豸�Խ�����Ӧ�豸�Խ�
//call��callback
//type:TYPE_MEET_INTERFACE_DEVICEOPER
//method: requestinvite��responseinvite
typedef struct
{
	Type_HeaderInfo hdr;
	int32u  encodemode;//��������  DEVICE_INVITECHAT_ENCODEMODE_HIGH
	int32u  inviteflag;//�豸�Խ���־ �μ�DEVICE_INVITE_FLAG_SIMPLEX
	int32u  operdeviceid;//������豸ID
}Type_DeviceChat, *pType_DeviceChat;

//call
//type:TYPE_MEET_INTERFACE_DEVICEOPER
//method:exitchat
typedef struct
{
	Type_HeaderInfo hdr;
	int32u  operdeviceid;//������豸ID
}Type_DoExitDeviceChat, *pType_DoExitDeviceChat;

//�յ�ֹͣ�豸�Խ�
//callback
//type:TYPE_MEET_INTERFACE_DEVICEOPER
//method: exitchat
typedef struct
{
	Type_HeaderInfo hdr;
	int32u  operdeviceid;//������豸ID
	int32u  exitdeviceid;//�˳����豸ID
}Type_ExitDeviceChat, *pType_ExitDeviceChat;

//castmode
#define NETBANDTEST_SINGLECASTSEND 0 //�������Ͳ���
#define NETBANDTEST_SINGLECASTRECV 1 //��������˲���
#define NETBANDTEST_MULTICASTSEND  2 //�鲥���Ͳ���
#define NETBANDTEST_MULTICASTRECV  3 //�鲥����˲���
#define NETBANDTEST_CLOSE		   4 //�رղ���
#define NETBANDTEST_IPLEN      64 //

//����������
//call
//type:TYPE_MEET_INTERFACE_DEVICEOPER
//method: METHOD_MEET_INTERFACE_DETAILINFO
typedef struct
{
	Type_HeaderInfo hdr;
	int32u  castmode;//NETBANDTEST_SINGLECAST
	int32u  band;	 //���� M eg:100M
	char	ip[NETBANDTEST_IPLEN];//��������ʱָ��
	int32u  totalseconds;	 //������ʱ�� �� eg:10 ��
}Type_NetBandTest, *pType_NetBandTest;

//������
//type:TYPE_MEET_INTERFACE_STREAMPLAY
//method: notify
//��Ϊѯ��ģʽʱ���ص���������1��ʾͬ�ⲥ��
typedef struct
{
	Type_HeaderInfo hdr;
	int32u  triggeruserval;//�μ����ļ��е�triggeruserval����
	int32u  deviceid;       //����������豸ID
	int32u  memberid;       //�����������ԱID
	int8u   substreamindex;       //���豸��ͨ����
	int8u   encodemode;////�μ� DEVICE_INVITECHAT_ENCODEMODE_HIGH
}Type_ReqStreamPush, *pType_ReqStreamPush;

//������
//type:TYPE_MEET_INTERFACE_STREAMPLAY
//method: requestpush
typedef struct
{
	Type_HeaderInfo hdr;
	int32u  triggeruserval;		  //�μ����ļ��е�triggeruserval����
	int32u  handledeviceid;       //�����������豸ID
	int8u   substreamindex;       //���豸��ͨ����
	int8u   encodemode;////�μ� DEVICE_INVITECHAT_ENCODEMODE_HIGH
	int    devnum; //�����豸
	//int32u deviceid[devnum];
}Type_DoReqStreamPush, *pType_DoReqStreamPush;

//������
//type:TYPE_MEET_INTERFACE_STREAMPLAY
//method: notify
//��Ϊѯ��ģʽʱ���ص���������1��ʾͬ�ⲥ��
typedef struct
{
	Type_HeaderInfo hdr;
	int32u  triggeruserval;//�μ����ļ��е�triggeruserval����
	int32u  deviceid;       //����������豸ID
	int8u   substreamindex; //���豸��ͨ����
	int8u   encodemode;////�μ� DEVICE_INVITECHAT_ENCODEMODE_HIGH
}Type_StreamPush, *pType_StreamPush;

//������
//type:TYPE_MEET_INTERFACE_STREAMPLAY
//method: push
typedef struct
{
	Type_HeaderInfo hdr;
	int32u  triggeruserval;//�μ����ļ��е�triggeruserval����
	int32u  deviceid;       //����������豸ID
	int8u   substreamindex; //���豸��ͨ����
	int8u   encodemode;////�μ� DEVICE_INVITECHAT_ENCODEMODE_HIGH
	int    devnum; //�����豸
	//int32u deviceid[devnum];
}Type_DoStreamPush, *pType_DoStreamPush;

//������֪ͨ
//callback
//type:TYPE_MEET_INTERFACE_STREAMPLAY
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;
	int32u  triggerid;//������ID
	int32u  createdeviceid;//����ִ�иô��������豸ID
	int32u  deviceid;//��Դ�豸ID
	int32u  subid;//��Դ�豸��ͨ��ID
	int8u   res;//�������õ���ԴID
	int32u  triggeruserval;//�μ����ļ��е�triggeruserval����
}Type_MeetStreamPlay, *pType_MeetStreamPlay;

//������
//call
//method: start
typedef struct
{
	Type_HeaderInfo hdr;

	int32u  encodemode;//�μ� DEVICE_INVITECHAT_ENCODEMODE_HIGH
	int32u  deviceid;//�豸ID
	int32u  subid;//�豸��ͨ��
	int32u  playflag;//���ű�־ MEDIA_PLAYFLAG_CLIENT
	int32u  triggeruserval;//�μ����ļ��е�triggeruserval����

	int	   resnum;
	int8u  res[MAX_RES_NUM];

	int    devnum; //�����豸
	//int32u deviceid[devnum];
}Type_MeetDoStreamPlay, *pType_MeetDoStreamPlay;

//��ѯ������
typedef struct
{
	int32u trrigerid;//������ID
	int32u  deviceid;//�豸ID
	int32u  subid;//�豸��ͨ��
}Item_MeetStreamPlayDetailInfo, *pItem_MeetStreamPlayDetailInfo;

//type:TYPE_MEET_INTERFACE_STREAMPLAY
//method: query/
typedef struct
{
	Type_HeaderInfo hdr;
	int32u	 num; //
}Type_MeetStreamPlayDetailInfo, *pType_MeetStreamPlayDetailInfo;

//ý�塢��ֹͣ����֪ͨ
//callback
//type:TYPE_MEET_INTERFACE_STOPPLAY
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;
	int32u  triggerid;
	int32u  createdeviceid;
	int8u   res;
}Type_MeetStopPlay, *pType_MeetStopPlay;

//��ѯ����ͳ��
//call
//type:TYPE_MEET_INTERFACE_MEETSTATISTIC
//method: query
typedef struct
{
	Type_HeaderInfo hdr;

	int32u meetingid;//����ID Ϊ0��ʾ��ѯ��ǰ����
}Type_MeetDoReqStatistic, *pType_MeetDoReqStatistic;

//���ز�ѯ����ͳ��֪ͨ
//callback
//type:TYPE_MEET_INTERFACE_MEETSTATISTIC
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;

	int32u meetingid;//����ID
	char   meetname[DEFAULT_DESCRIBE_LENG];//��������
	int32u streamgetcount;//��������
	int32u screengetcount;//ͬ��������
	int32u filegetcount;//�ļ�������
	int32u chatcount;//��������
	int32u servicegetcount;//����������
	int32u whiteboardopencount;//�װ巢�����
	int32u whiteboardusecount;//�װ彻������
	int32u votecount;//ͶƱ�������
	int32u electioncount;//ѡ�ٷ������
	int32u questioncount;//�ʾ���鷢�����
	int32u bulletcount;//���淢�����
	int64u addtime;//����ʱ�� ��
}Type_MeetStatisticInfo, *pType_MeetStatisticInfo;

//ʱ��λ���ͳ��
//quartertype
#define MEET_QUERYSTATISTIC_BYMONTH	  1//���·ݲ�ѯ,���һ�οɲ�12����
#define MEET_QUERYSTATISTIC_BYQUARTER 2//�����Ȳ�ѯ,���һ�οɲ�12������
#define MEET_QUERYSTATISTIC_BYYEAR	  3//�����ѯ,���һ�οɲ�12����


//��ʱ��β�ѯ����ͳ��
//call
//type:TYPE_MEET_INTERFACE_MEETSTATISTIC
//method: ask
typedef struct
{
	Type_HeaderInfo hdr;
	
	int32u quartertype;//�μ� quartertype����

	//ͳ��ʱ���
	int16u startyear; //
	int16u startmonth; ////���²�ѯ����Ч
	int16u endyear; //
	int16u endmonth; ////���²�ѯ����Ч
}Type_QueryQuarterStatistic, *pType_QueryQuarterStatistic;

typedef struct
{
	//ͳ��ʱ���
	int16u startyear; //
	int16u startmonth; //
	int16u endyear; //
	int16u endmonth; //

	int32u meetingcount;//�ܻ�����

	//�ܼ���
	int32u streamgetcount;//��������
	int32u screengetcount;//ͬ��������
	int32u filegetcount;//�ļ�������
	int32u chatcount;//��������
	int32u servicegetcount;//����������
	int32u whiteboardopencount;//�װ巢�����
	int32u whiteboardusecount;//�װ彻������
	int32u votecount;//ͶƱ�������
	int32u electioncount;//ѡ�ٷ������
	int32u questioncount;//�ʾ���鷢�����
	int32u bulletcount;//���淢�����

}Item_MeetOneStatistic, *pItem_MeetOneStatistic;

//���ذ�ʱ��β�ѯ����ͳ��
//callback
//type:TYPE_MEET_INTERFACE_MEETSTATISTIC
//method: ask
typedef struct
{
	Type_HeaderInfo hdr;

	int32u quartertype;//
	int32u num;
	pItem_MeetOneStatistic pitem;
}Type_MeetQuarterStatisticInfo, *pType_MeetQuarterStatisticInfo;

#ifndef MEET_FACEID_MAINBG
#define MEET_FACEID_MAINBG
//���ز�ѯ��������
//�����������
//faceid
#define MEET_FACEID_MAINBG			1  //������ png  //��ID
#define MEET_FACEID_SUBBG			2  //�ӽ��� png  //��ID
#define MEET_FACEID_LOGO			3  //logo png    //��ID
#define MEET_FACEID_MEETNAME		4  //�������� text
#define MEET_FACEID_MEMBERNAME		5  //�λ������� text
#define MEET_FACEID_MEMBERCOMPANY	6  //�λ��˵�λ text
#define MEET_FACEID_MEMBERJOB		7  //�λ���ְҵ text
#define MEET_FACEID_SEATNAME		8  //��ϯ���� text
#define MEET_FACEID_TIMER			9  //����ʱ�� text
#define MEET_FACEID_COMPANY			10 //��λ���� text
#define MEET_FACEID_SHOWFILE		11 //����Ԥ���ļ�����Ƶ��  //��ID
#define MEET_FACEID_COLTDTEXT		12 //��˾���� onytext

#define MEET_FACE_LOGO_GEO		13 //text
#define MEET_FACE_topstatus_GEO		14 //����״̬ text 
#define MEET_FACE_checkin_GEO		15 //������鰴ť text
#define MEET_FACE_manage_GEO		16 //�����̨ text
#define MEET_FACE_remark_GEO		17 //��ע text
#define MEET_FACE_role_GEO		18 //��ɫ text
#define MEET_FACE_ver_GEO		19 //�汾 text

#define MEET_FACE_SeatIcoShow_GEO		20 //��λͼ���Ƿ���ʾ text

#define MEET_FACE_SeatLayoutShow_top		21 //������ʾ��  text
#define MEET_FACE_SeatLayoutShow_main		22 //������ʾ��  text
#define MEET_FACE_SeatLayoutShow_bottom		23 //������ʾ��  text

#define  MEET_FACE_MEETAGENDAFont         24

#define MEET_FACE_BulletinBK        25  // ���鹫�汳��  png ����ID��
#define MEET_FACE_BulletinLogo      26  // ���鹫��LOGO  png ����ID��
#define MEET_FACE_BulletinTitle     27  // ���鹫�����  text
#define MEET_FACE_BulletinContent   28  // ���鹫������  text
#define MEET_FACE_BulletinBtn       29  // ����رհ�ť  text


#define MEET_FACEID_PROJECTIVE_MIANBG		101  //ͶӰ���� png
#define MEET_FACEID_PROJECTIVE_LOGO			102  //logo png
#define MEET_FACEID_PROJECTIVE_MEETNAME		103  //�������� text
#define MEET_FACEID_PROJECTIVE_SEATNAME		104  //��ϯ���� text
#define MEET_FACEID_PROJECTIVE_TIMER		105  //����ʱ�� text
#define MEET_FACEID_PROJECTIVE_COMPANY		106  //��λ���� text
#define MEET_FACEID_PROJECTIVE_SIGNINFO		107  //ǩ����� text  // ���� ���ĳɷֿ���ʾ��
#define MEET_FACEID_PROJECTIVE_MEETTIME		108  //����ʱ�� text
#define MEET_FACEID_PROJECTIVE_COMPANYNAME	109  //��˾����λ�� text
#define MEET_FACEID_PROJECTIVE_STATUS		110  //����״̬ text

#define MEET_FACEID_PROJECTIVE_SHOWFILE		111  //����Ԥ���ļ�����Ƶ��  //��ID
#define MEET_FACEID_PROJECTIVE_COLTDTEXT	112  //��˾���� onytext

#define MEET_FACEID_PROJECTIVE_SIGN_ALL     113  // ǩ����Ӧ��
#define MEET_FACEID_PROJECTIVE_SIGN_IN      114  // ǩ�����ѵ�
#define MEET_FACEID_PROJECTIVE_SIGN_OUT     115  // ǩ����δ��


//fontflag
#define MEET_FONTFLAG_BOLD		0x00000001 //�Ӵ�
#define MEET_FONTFLAG_LEAN		0x00000002 //��б
#define MEET_FONTFLAG_UNDERLINE 0x00000004 //�»���

//flag
#define MEET_FACEFLAG_SHOW			0x00000001 //��λ���ڱ�ʾ�����Ƿ�ɼ�
#define MEET_FACEFLAG_TEXT			0x00000002 //��λ���ڱ�ʾ�������ı�����,����Ϊ�ļ�ID
#define MEET_FACEFLAG_ONLYTEXT		0x00000004 //��λ���ڱ�ʾ�����Ǵ��ı�����
#endif

//�ı���
typedef struct
{
	unsigned int faceid;  //������ID
	unsigned int flag;	  //����ֵ

	/* ���������С���㷽��
	fontsize * ��ǰ��Ļ�߶� / ��׼��Ļ�߶�
	eg: �յ������С��100����ǰ����Ļ�߶���720���������ʵ�������СΪ��100 * 720 / 1080 = 67
	*/
	unsigned short  fontsize;	//�����С
	unsigned int    color;		//����rgba��ɫ
	unsigned short  align;		//���� �μ����ļ���font align flag
	unsigned short  fontflag;	//��������
	char	fontname[FONTNAME_LENG];//��������

	float lx;//���� ���Ͻ�x  (x * 100 / width)
	float ly;//���� ���Ͻ�y  (y * 100 / height)
	float bx;//���� ���½�x
	float by;//���� ���½�y

}Item_FaceTextItemInfo, *pItem_FaceTextItemInfo;

//ͼƬ��
typedef struct
{
	unsigned int faceid;  //������ID
	unsigned int flag;    //����ֵ
	unsigned int mediaid; //��ֵ
}Item_FacePictureItemInfo, *pItem_FacePictureItemInfo;

//���ı���
typedef struct
{
	unsigned int faceid;  //������ID
	unsigned int flag;    //����ֵ
	char		 text[DEFAULT_DESCRIBE_LENG];//�ı�
}Item_FaceOnlyTextItemInfo, *pItem_FaceOnlyTextItemInfo;

//call
//type:TYPE_MEET_INTERFACE_MEETFACECONFIG
//method: query modify
typedef struct
{
	Type_HeaderInfo hdr;

	int num;
}Type_FaceConfigInfo, *pType_FaceConfigInfo;

//callback
//TYPE_MEET_INTERFACE_MEETFACECONFIG
//method:notify
typedef struct
{
	Type_HeaderInfo hdr;

	int32u opermethod;//����֪ͨ����Ϊopermethod����������
	int32u id;//���ָ����ID�ձ�ʾ�Ƕ��ض�ID�Ĳ���,Ϊ0��ʾȫ��
}Type_meetFaceConfigInfo, *pType_meetFaceConfigInfo;


//ȫ���ִ�
#define MEET_PUBLICINFO_DATALEN 260 

//dataid
#define MEET_MAXHUB_SERVERURL       2 //MAXHUB�װ�ϵͳ�ķ�������ַ {"scanServerIp":"10.248.6.208","scanServerPort":"8889","serverIp":"10.248.6.11","serverPort":"61000"}
#define MEET_PUBLIINFO_DATACACHE	3 //�������ϻ��� {"enable":"1","size":"150"} enable:�Ƿ����� size:����ָ����С�� ��λ:M
#define MEET_PUBLIINFO_MEETRECYCLE	4 //���鶨������ {"enable":"1","day":"7"} enable:�Ƿ����� day:����ָ������ǰ�� ��λ:��

//callback
//TYPE_MEET_INTERFACE_PUBLICINFO
//method:notify
typedef struct
{
	Type_HeaderInfo hdr;

	int32u opermethod;//����֪ͨ����Ϊopermethod����������
	int32u id;//���ָ����ID�ձ�ʾ�Ƕ��ض�ID�Ĳ���,Ϊ0��ʾȫ��
}Type_meetNotifyPublicInfo, *pType_meetNotifyPublicInfo;

//��ѯϵͳȫ���ִ�
//call
//TYPE_MEET_INTERFACE_PUBLICINFO
//method:notify/query(call)
typedef struct
{
	Type_HeaderInfo hdr;

	int num;//

	//int32u dataid[num];
}Type_meetQueryPublicInfo, *pType_meetQueryPublicInfo;

//���ز�ѯϵͳȫ���ִ�
//����ȫ���ִ�
typedef struct
{
	int32u dataid;//�ִ�ID
	char   dataval[MEET_PUBLICINFO_DATALEN];
}Item_meetPublicInfo, *pItem_meetPublicInfo;

//call
//TYPE_MEET_INTERFACE_PUBLICINFO
//method:set/query(return)
typedef struct
{
	Type_HeaderInfo hdr;
	int num;//

	//Item_meetPublicInfo item[num];
}Type_meetPublicInfo, *pType_meetPublicInfo;

//call
//type:TYPE_MEET_INTERFACE_MEETSENDMAIL
//method: send

#define MEET_MAIL_PATHLEN 260
typedef struct
{
	char filepath[MEET_MAIL_PATHLEN];
}Item_meetMailPathInfo, *pItem_meetMailPathInfo;

//type
#define MEET_MAIL_SENDTYPE_TO  0 //����
#define MEET_MAIL_SENDTYPE_CC  1 //����
#define MEET_MAIL_SENDTYPE_BCC 2 //���ܷ���

typedef struct
{
	int32u userid;
	int	   type;//
}Item_meetMailUserInfo, *pItem_meetMailUserInfo;

typedef struct
{
	Type_HeaderInfo hdr;

	const char* subject;//����
	const char* bodytext;//����

	//file
	int filenum;
	pItem_meetMailPathInfo pfile;

	//recive member
	int     useridnum;
	pItem_meetMailUserInfo puser;
}Type_SendMailInfo, *pType_SendMailInfo;

//callback
//TYPE_MEET_INTERFACE_MEETSENDMAIL
//method:notify

#define MEET_MAIL_SENDSTATE_OK	  0 //���ͳɹ�
#define MEET_MAIL_SENDSTATE_ERROR 1 //����ʧ��

#define MEET_MAIL_STATEINFO 1024
typedef struct
{
	Type_HeaderInfo hdr;

	int  error;
	char stateinfo[MEET_MAIL_STATEINFO];//�ʼ�����״̬
}Type_meetMailInfo, *pType_meetMailInfo;

/*--------------------------------�ļ��Զ���ѡ������ 20180723-----------------------------------------*/
#define MEET_FILESCORE_VOTECONTENT_MAXLEN 200 //�ļ����ֱ�����󳤶�
#define MEET_FILESCORE_MAXITEM 4				 //�ļ�����ѡ��������
#define MEET_FILESCORE_MAXITEM_LEN 60				 //�ļ�����ѡ����󳤶�
typedef struct
{
	int32u voteid;//������ID��������Ψһ��������ʶɾ�����޸ġ�����ֹͣ�Ȳ���
	int32u fileid;//�ļ�ID
	char   content[MEET_FILESCORE_VOTECONTENT_MAXLEN]; //ͶƱ���� 

	int32u mode; //����ͶƱ=0 ����ͶƱ=1 �μ�VOTEMODE_agonymous

	int32u votestate;     //ͶƱ״̬  �μ� vote_notvote
	int32u timeouts;     //��ʱ���� ��λ����
	int64u starttime;//��ʱͶƱ��ʱ�� UTC��
	int64u endtime;//��ʱͶƱ��ʱ�� UTC��

	int32u selectcount;     //��Чѡ������
	int32u shouldmembernum;  //Ӧ������
	int32u realmembernum;    //��Ͷ����
	int32u itemsumscore[MEET_FILESCORE_MAXITEM];  //ÿ��ѡ���Ŀǰ�ܷ�--������Ͷ��������
	char   voteText[MEET_FILESCORE_MAXITEM][MEET_FILESCORE_MAXITEM_LEN];  //��������
}Type_Item_UserDefineFileScore, *pType_Item_UserDefineFileScore;

//�ļ��Զ���ѡ������
//call
//type:TYPE_MEET_INTERFACE_FILESCOREVOTE
//method: add\modify\query
typedef struct
{
	Type_HeaderInfo hdr;

	int32u fileid;//��ѯָ���ļ��� Ϊ0��ʾ��ѯ����

	int32u num;
	//Type_Item_UserDefineFileScore [num]; 
}Type_UserDefineFileScore, *pType_UserDefineFileScore;

//�ļ��Զ���ѡ�����ֲ�ѯ���ر��֪ͨ
//callback
//type:TYPE_MEET_INTERFACE_FILESCOREVOTE\TYPE_MEET_INTERFACE_FILESCOREVOTESIGN
//method: METHOD_MEET_INTERFACE_NOTIFY
typedef struct
{
	Type_HeaderInfo hdr;
	int32u opermethod;
	int32u voteid;
}Type_UserDefineFileScoreQueryNotify, *pType_UserDefineFileScoreQueryNotify;

//ɾ���ļ��Զ���ѡ������
//call
//type:TYPE_MEET_INTERFACE_FILESCOREVOTE
//method: delete\stop
typedef struct
{
	Type_HeaderInfo hdr;
	int32u	num;
	//int32u voteid[num];//ͶƱID
}Type_DeleteUserDefineFileScore, *pType_DeleteUserDefineFileScore;

typedef struct
{
	int32u	voteid;
	int32u	voteflag;//�μ� MEET_VOTING_FLAG_REVOTE
	int32u	timeouts;
	int32u	membernum;
	//int32u memberid[membernum];//��ԱID
}Type_Item_StartUserDefineFileScore, *pType_Item_StartUserDefineFileScore;

//�����ļ��Զ���ѡ������
//call
//type:TYPE_MEET_INTERFACE_FILESCOREVOTE
//method: start
typedef struct
{
	Type_HeaderInfo hdr;
	Type_Item_StartUserDefineFileScore item;
}Type_StartUserDefineFileScore, *pType_StartUserDefineFileScore;

//�յ������ļ��Զ���ѡ������
//callback
//type:TYPE_MEET_INTERFACE_FILESCOREVOTE
//method: start
typedef struct
{
	Type_HeaderInfo hdr;
	int32u voteid;//������ID��������Ψһ��������ʶɾ�����޸ġ�����ֹͣ�Ȳ���
	int32u fileid;//�ļ�ID
	char   content[MEET_FILESCORE_VOTECONTENT_MAXLEN]; //ͶƱ���� 

	int32u mode; //����ͶƱ=0 ����ͶƱ=1 �μ�VOTEMODE_agonymous
	int32u timeouts; //��ʱ���� ��λ����
	int64u starttime;//��ʱͶƱ��ʱ�� UTC��
	int32u voteflag;//�μ� MEET_VOTING_FLAG_REVOTE

	int32u selectcount;     //��Чѡ������
	char   voteText[MEET_FILESCORE_MAXITEM][MEET_FILESCORE_MAXITEM_LEN];  //��������

	int32u membernum;
	int32u* pmemberids;// [membernum];//��ԱID
}Type_StartUserDefineFileScoreNotify, *pType_StartUserDefineFileScoreNotify;

typedef struct
{
	int32u memberid;  //��ԱID
	int32u state;  //�Ƿ��Ѿ��ύ 1��ʾ�Ѿ��ύ 0δ�ύ
	int32u score[MEET_FILESCORE_MAXITEM];  //ÿ��ѡ��ķ�--������Ͷ��������
	int64u votetime;//�ύʱ�� UTC ��
	char   content[MEET_FILESCORE_VOTECONTENT_MAXLEN]; //��� 

}Type_Item_FileScoreMemberStatistic, *pType_Item_FileScoreMemberStatistic;

//�ļ��Զ���ѡ��������Աͳ��
//call
//type:TYPE_MEET_INTERFACE_FILESCOREVOTESIGN
//method: query
typedef struct
{
	Type_HeaderInfo hdr;

	int32u    voteid;
	int		  num;
	//Type_Item_FileScoreMemberStatistic[num];
}Type_UserDefineFileScoreMemberStatistic, *pType_UserDefineFileScoreMemberStatistic;

//�ļ��Զ���ѡ��������Աͳ��
//call
//type:TYPE_MEET_INTERFACE_FILESCOREVOTESIGN
//method:METHOD_MEET_INTERFACE_SUBMIT
typedef struct
{
	Type_HeaderInfo hdr;

	int32u voteid;
	int32u memberid;//��ԱID
	int32u score[MEET_FILESCORE_MAXITEM];  //ÿ��ѡ��ķ�--������Ͷ��������
	char   content[MEET_FILESCORE_VOTECONTENT_MAXLEN]; //��� 
}Type_UserDefineFileScoreMemberStatisticNotify, *pType_UserDefineFileScoreMemberStatisticNotify;

//��ѯ����ͶƱ��������Ϣ
//property id
#define MEETFILESCOREVOTE_PROPERTY_HADSIGNIN			    1 //��ͶƱID\��ԱID�����Ƿ��Ѿ�ͶƱ query
#define MEETFILESCOREVOTE_PROPERTY_ATTENDNUM		        2 //��ͶƱID\����ͶƱӦ��������  query
#define MEETFILESCOREVOTE_PROPERTY_VOTEDNUM		            3 //��ͶƱID\����ͶƱ�Ѿ�ͶƱ������  query


//�ļ��Զ���ѡ��������Աͳ������
//call
//type:TYPE_MEET_INTERFACE_FILESCOREVOTESIGN
//method: queryproperty
typedef struct
{
	Type_HeaderInfo hdr;

	int32u propertyid;//����ID
	int32u voteid;//�������
	int32u memberid;//������� Ϊ0��ʾ����
	int32u propertyval;//����ֵ 

}Type_MeetFileScoreVoteQueryProperty, *pType_MeetFileScoreVoteQueryProperty;

//������֤ɾ��
typedef struct
{
	int32u memberid;//��ʶ��-��ѯ����᷵�ظ�ֵ Ϊ0��ʾ��ѯȫ��ָ��
	char   phone[DEFAULT_SHORT_DESCRIBE_LENG];  //�绰-��ѯ����᷵�ظ�ֵ
}Item_ZKIdentify_SingleItem, *pItem_ZKIdentify_SingleItem;

//call
//type:TYPE_MEET_INTERFACE_ZKIDENTIFY
//method:del,query
typedef struct
{
	Type_HeaderInfo hdr;

	int num; //������Ϊɾ��ʱ,Ϊ0��ʾɾ��ȫ��,������Ϊqueryʱ,Ϊ0��ʾ��ѯȫ��

	//Item_ZKIdentify_SingleItem item[];
}Type_ZKIdentify_Simple, *pType_ZKIdentify_Simple;

//������֤
//callback
//type:TYPE_MEET_INTERFACE_ZKIDENTIFY
//method: query��callback��
typedef struct
{
	Type_HeaderInfo hdr;

	int    fingerimg1len;//1��ָָ��ģ�� Ϊ0��ʾû��ģ������
	char*  pfingerimg1;

	int    fingerimg2len;//2��ָָ��ģ�� Ϊ0��ʾû��ģ������
	char*  pfingerimg2;

	int    faceimglen;//����ģ�� Ϊ0��ʾû��ģ������
	char*  pfaceimg;

	int64u addtime;//¼���ʱ��

	Item_ZKIdentify_SingleItem base;

}Type_ZKIdentify_QueryReturn, *pType_ZKIdentify_QueryReturn;

//������֤
//call,callback
//type:TYPE_MEET_INTERFACE_ZKIDENTIFY
//method: modify(call)
typedef struct
{
	Type_HeaderInfo hdr;

	int    fingerimg1len;//1��ָָ��ģ�� Ϊ0��ʾû��ģ������
	int    fingerimg2len;//2��ָָ��ģ�� Ϊ0��ʾû��ģ������
	int    faceimglen;//����ģ�� Ϊ0��ʾû��ģ������

	Item_ZKIdentify_SingleItem base;

}Type_ZKIdentify_Oper, *pType_ZKIdentify_Oper;


// ���ŷ���
#define  MAX_PHONE_NUMBER     100    // һ�οɷ��͵��������
#define  PHONE_NUMBER_LEN     12     // �ֻ����볤��
#define  MAX_MSG_LEN          300    // ��������ַ���

// call
// type: TYPE_MEET_INTERFACE_SMSSERVICE
// method: SEND, QUERY, TEXTMSG, GETSIZE
typedef struct  
{
	Type_HeaderInfo  hdr;

	char  serverIP[DEFAULT_SHORT_DESCRIBE_LENG];        // ������IP
	char  serverPort[DEFAULT_SHORT_DESCRIBE_LENG];      // �������˿ں�
	char  ctrlType[DEFAULT_SHORT_DESCRIBE_LENG];        // ���ŷ������ͣ����ͣ���ѯ�� ��������Ӧ����ҳ��

	char  uname[DEFAULT_SHORT_DESCRIBE_LENG];           // ���ŷ����˻� ����
	char  upwd[DEFAULT_SHORT_DESCRIBE_LENG];            // ���ŷ����˻� ����
	char  numbers[MAX_PHONE_NUMBER][PHONE_NUMBER_LEN];  // �绰����  �����Ͷ��ű����������ɲ��
	char  messageInfo[MAX_MSG_LEN];                     // ��������  �����Ͷ��ű����������ɲ��
}Type_SMSInfo, *pType_SMSInfo;

/* ���ŷ���״̬�� */
#define SMS_SEND_STATUS_SUCCESS    1  // ���ͳɹ�
#define SMS_SEND_STATUS_FAILED     2  // ����ʧ��
#define SMS_SEND_STATUS_BANSEND    3  // ��������
#define SMS_SEND_STATUS_MISTAKE    4  // ͨ�����

// item query SMS send status info
typedef struct
{
	int  id;       // �ύ����ʱ���ص����α�ʶID
	int  status;   // ���ص�״̬�루1���ɹ���2��ʧ�ܣ�3���������룬4��ͨ�����
	char number[PHONE_NUMBER_LEN];   // �ֻ�����
	char donedate[DEFAULT_SHORT_DESCRIBE_LENG];  // �ظ�ʱ��
}Item_SMSSendStatus, *pItem_SMSSendStatus;

// recv
// type: TYPE_MEET_INTERFACE_SMSSERVICE
// method: QUERY ����ѯ����״̬��������������ݣ�
typedef struct  
{
	Type_HeaderInfo  hdr;

	// ״̬�룺0��������1�ύ�ɹ���2�û��������벻��ȷ��3���ʷ����쳣��4������Դ����
	int  statuscode;  // ״̬��
	int  total;       // ���պ�����������
	Item_SMSSendStatus listSendStatus[MAX_PHONE_NUMBER];  // ÿ������ķ���״̬
}Type_SMSSendStatus, *pType_SMSSendStatus;


typedef struct 
{
	char  number[PHONE_NUMBER_LEN];  // �ֻ�����
	char  msg[MAX_MSG_LEN];          // �ն˻ظ�����
	char  donedate[DEFAULT_SHORT_DESCRIBE_LENG];  // �ظ�ʱ��
}Item_SMSRecvMsg, *pItem_SMSRecvMsg;
// recv
// type: TYPE_MEET_INTERFACE_SMSSERVICE
// method: TEXTMSG����ѯ�ظ���������������ݣ�
typedef struct
{
	Type_HeaderInfo  hdr;

	int  statuscode;  // ״̬��
	int  total;       // ���պ�����������
	Item_SMSRecvMsg  listRecvMsg[MAX_PHONE_NUMBER];  // �ն˻ظ�����Ϣ
}Type_SMSRecvMsg, *pType_SMSRecvMsg;


// recv
// type: TYPE_MEET_INTERFACE_SMSSERVICE
// method: GETSIZE����ѯ������������������ݣ�
typedef struct  
{
	Type_HeaderInfo  hdr;

	int  statuscode;  // ״̬��
	int  account;     // ʣ���������
	char tariff[DEFAULT_SHORT_DESCRIBE_LENG];  // �ʷѣ�Ԫ/����
}Type_SMSLastCount, *pType_SMSLastCount;


//������°汾��������ɺ�֪ͨӦ�ò����������ͬʱҪ�Ѱ汾��д��client.ini�ļ���
//callback
//type:TYPE_MEET_INTERFACE_UPDATE
//method: METHOD_MEET_INTERFACE_UPDATE
typedef struct
{
	Type_HeaderInfo hdr;

	int16u localhardver;//��ǰclient.ini��¼�İ汾
	int16u localsoftver;//��ǰclient.ini��¼�İ汾
	int16u newhardver;//��������Ӳ���汾
	int16u newsoftver;//������������汾
	int16u newminhardver;//�����������Ӳ���汾
	int16u newminsoftver;//���������������汾
	int32u deviceidtype;////���������豸���� eg:0x1100000
	int8u  devicemask;//���������豸������  eg:14
	int8u  fill[3];

	char   updatepath[MEET_PATH_MAXLEN]; //��������ѹ���Ŀ¼  ע��������Ҫ�д���Ŀ¼��Ȩ��

}Type_MeetUpdateNotify, *pType_MeetUpdateNotify;

//////////////////////////// topic ���� //////////////////////////////////////////////
//�᳡����
//callback
//type:TYPE_MEET_INTERFACE_TOPIC
//method: notify
typedef struct
{
	Type_HeaderInfo hdr;
	int32u opermethod;//����֪ͨ����Ϊopermethod����������
	int32u id;//���ָ����ID�ձ�ʾ�Ƕ��ض�ID�Ĳ���,Ϊ0��ʾȫ��

}Type_MeetTopicNotify, *pType_MeetTopicNotify;

//status
#define MEET_TOPIC_STATUS_OFF		 0 //δ��ʼ
#define MEET_TOPIC_STATUS_ON         1 //��ʼ

typedef struct
{
	int32u  topiciid;  //����ID
	int32u  status;  //����״̬ �μ� MEET_TOPIC_STATUS_OFF
	int32u  fileid;  //���ļ�ID
	int64u	startutctime;//��λ��
	int64u	endutctime;  //��λ��
	char	topicname[DEFAULT_DESCRIBE_LENG]; //��������
	char	reporter[DEFAULT_NAME_MAXLEN];//�㱨��λ
}Item_TopicItemInfo, *pItem_TopicItemInfo;

//call
//type:TYPE_MEET_INTERFACE_TOPIC
//method: query\add\modify\del
typedef struct
{
	Type_HeaderInfo hdr;

	int num;
	//Item_TopicItemInfo; //����ǰ��������дItem_TopicItemInfo

}Type_MeetTopics, *pType_MeetTopics;

//call
//type:TYPE_MEET_INTERFACE_TOPIC
//method: METHOD_MEET_INTERFACE_SAVE
typedef struct
{
	Type_HeaderInfo hdr;

	int32u num;//
	//int32u topicids[num];//����ID��˳��
}Type_SetMeetTopicPos, *pType_SetMeetTopicPos;

//////////////////////////// topic group //////////////////////////////////////////////
typedef struct
{
	int32u  topiciid;  //����ID
	int32u  groupid;  //����ID
	char	groupname[DEFAULT_NAME_MAXLEN];//��������
}Item_TopicGroupItemInfo, *pItem_TopicGroupItemInfo;

//call
//type:TYPE_MEET_INTERFACE_TOPICGROUP
//method: query\add\modify\del
typedef struct
{
	Type_HeaderInfo hdr;

	int num;
	int32u topicid;
	//Item_TopicGroupItemInfo; //����ǰ��������дItem_TopicGroupItemInfo

}Type_MeetTopicGroups, *pType_MeetTopicGroups;

//////////////////////////// topic nopermission //////////////////////////////////////////////
typedef struct
{
	int32u  topiciid;  //����ID
	int32u  memberid;  //�λ���ԱID
}Item_TopicPermItemInfo, *pItem_TopicPermItemInfo;

//call
//type:TYPE_MEET_INTERFACE_TOPICPERMISSION
//method: query\add\modify\del
typedef struct
{
	Type_HeaderInfo hdr;

	int num;
	int32u topicid;
	//Item_TopicPermItemInfo; //����ǰ��������дItem_TopicPermItemInfo

}Type_MeetTopicPerms, *pType_MeetTopicPerms;

///////////////////////////////Meet Task//////////////////////////////////////
//��ѯ���鷢������
typedef struct
{
	int32u taskid;//����ID
	char   taskname[MEET_MAX_TASKNAMELEN];//��������
}Item_MeetTaskInfo, *pItem_MeetTaskInfo;

//call
//type:TYPE_MEET_INTERFACE_MEETTASK
//method: query
typedef struct
{
	Type_HeaderInfo hdr;
	int32u	 num; //
}Type_MeetTaskInfo, *pType_MeetTaskInfo;

//��ѯָ��ID�Ļ��鷢������
typedef struct
{
	int32u mediaid;//ý��ID
	char   name[DEFAULT_DESCRIBE_LENG];//add/modify����Ҫ���ø���
}Item_MediaTaskDetailInfo, *pItem_MediaTaskDetailInfo;

typedef struct
{
	int32u deviceid;//�豸ID
	char   name[DEFAULT_DESCRIBE_LENG];//add/modify����Ҫ���ø���
}Item_DeviceTaskDetailInfo, *pItem_DeviceTaskDetailInfo;

typedef struct
{
	int32u taskid;//����ID -->��ѯ��ɾ��ֻ��Ҫָ��ID���� ��ӳɹ���᷵������ID����ֱ�Ӹ��½��治�������²�ѯ
	char   taskname[MEET_MAX_TASKNAMELEN];//��������
	int    playmode;//=1 ������� =2 ˳�򲥷�
	int    medianum;
	int    devicenum;
	//Item_MediaTaskDetailInfo [medianum]
	//Item_DeviceTaskDetailInfo[devicenum]
}Item_MeetTaskDetailInfo, *pItem_MeetTaskDetailInfo;

//call
//type:TYPE_MEET_INTERFACE_MEETTASK
//method: 
//METHOD_MEET_INTERFACE_SINGLEQUERYBYID|delete|start|stopʹ��TypeQueryInfoByID 
//add|modifyʹ��Type_MeetTaskDetailInfo(add|updateһ��ֻ����һ��)
typedef struct
{
	Type_HeaderInfo hdr;
	int32u	 num; //
}Type_MeetTaskDetailInfo, *pType_MeetTaskDetailInfo;

#pragma pack(pop)
// #ifdef __cplusplus
// }
// #endif

#endif