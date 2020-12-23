#ifndef _COMMDEF_H_
#define _COMMDEF_H_

#include "base.h"

#pragma pack(push)
#pragma pack(1)


#define WALLET_NETPACKETHEADER  0xeff7ff07		//ƽ̨���ݰ���ʶ ---20151211
#define COMMVER					2		//ͨѶ��ʽ�汾��

#define MSG_THREAD_QUIT			50000


//������240-255�����������ʹ�ã�Ӧ�ò㲻��ʹ��

#define	FUNC_QUERYATTRIB		1		//һ���Բ�ѯ�Ĵ���
#define FUNC_LOGINATTRIBPUSH	2		//�Ǽ�PUSH��ѯ
#define FUNC_LOGOUTATTRIBPUSH	3		//ע��PUSH��ѯ
#define FUNC_UPDATEATTRIB		4		//���¼Ĵ���ֵ
#define	FUNC_QUERYMEDIAFILE		5		//��ȡý���ļ���Ӧ��
#define FUNC_LOGINMEDIAFILE		6		//����ý���ļ�
#define FUNC_LOGOUTMEDIAFILE	7		//ɾ��ý���ļ�
#define FUNC_UPDATEMEDIAFILE	8		//�����ļ���Ϣ
#define	FUNC_LOGINEVENT			9		//ע���¼�
#define	FUNC_LOGOUTEVENT		10		//ע���¼�
#define	FUNC_UPDATEEVENT		11		//�����¼�
#define FUNC_LOGINTRIGGER		12		//ע�ᴥ����
#define	FUNC_LOGOUTTRIGGER		13		//ע��������
#define	FUNC_UPDATETRIGGER		14		//���´�����
#define	FUNC_TRIGGEREXEC		15		//��������������
#define	FUNC_LOGINDEVICEGROUP	17		//ע���豸��
#define	FUNC_LOGOUTDEVICEGROUP	18		//ע���豸��
#define	FUNC_UPDATEDEVICEGROUP	19		//�����豸��
#define	FUNC_LOGINUSER			20		//ע���û�
#define	FUNC_LOGOUTUSER			21		//ע���û�
#define	FUNC_UPDATEUSER			22		//�����û�
#define	FUNC_WRITEDB			23		//д����������ݿ�
#define	FUNC_READDB				24		//�ӷ��������ݿ��ж�ȡ
#define	FUNC_WRITELOG			25		//дLOG
#define	FUNC_READLOG			26		//��LOG
#define	FUNC_PLAYMEDIA			27		//����ý���ļ�
#define	FUNC_STARTEXCHANGE		28		//�豸��������
#define	FUNC_PLAYSTREAM			29		//������
#define FUNC_GETEVENTINFOSINGLE		30		//��ȡ�¼���Ϣ,ID��
#define	FUNC_LOGINDEVICE		31		//�豸�Ǽ�
#define	FUNC_LOGOUTDEVICE		32		//�豸ע��
#define	FUNC_UPDATE				33		//�����豸���
#define FUNC_BINDUSER			34		//���û��󶨵����豸�ϣ����ڵ�¼
#define FUNC_EXITUSER			35		//�û��˳���¼
#define	FUNC_DELDB				36		//ɾ�����ݿ��ڵ�����
#define FUNC_PUTMEDIA			37		//ָʾ�ϴ�ý���ļ�ʱ��ý��ID�ź�ý�������ID�ţ���������������������豸
#define FUNC_RETURN				38		//�ɷ��������͸��նˣ����ڵǼ�ע�᷵��ID�š�������ID������
#define FUNC_PUTSTREAM			39		//ָʾ��Դ�ն������������������
#define FUNC_GETTRIGGERINFOSINGLE		40		//��ȡ��������Ϣ��ID��ȡ
#define FUNC_GETMULTICASTADDR	41		//�������������鲥��ַ
#define FUNC_FREEMULTICASTADDR	42		//���������ͷ��鲥��ַ
#define FUNC_MEDIAISBEING		43		//ý����������͸��������������ʾ������ڵ�ý���ļ�
#define FUNC_MEDIADELBEING		44		//ý����������͸��������������ʾ����ɾ����ý���ļ�
#define FUNC_GETMEDIAINFOSINGLE	45		//��ȡý���ļ���Ϣ,��ID��ȡ
#define FUNC_GETMEDIAINFO		46		//��ȡý���ļ���Ϣ,ID������ֵ��ȡ
#define FUNC_GETTRIGGERINFO		47		//��ȡ��������Ϣ
#define FUNC_GETDEVICEGROUPINFO	48		//��ȡ�豸����Ϣ
#define FUNC_GETEVENTINFO		49		//��ȡ�¼���Ϣ
#define FUNC_GETSTREAMINFO		50		//��ȡ��ͨ����Ϣ
#define FUNC_GETUSERINFO		51		//��ȡ�û�ͨ����Ϣ
#define FUNC_SETDEVICESYSTEATTRIB	52	//�����豸��ϵͳ���ԣ�63�żĴ�����
#define FUNC_LOGINMEDIAATTRIB			53		//����ý������
#define FUNC_LOGOUTMEDIAATTRIB			54		//ɾ��ý������
#define FUNC_UPDATEMEDIAATTRIB			55		//����ý��������Ϣ
#define FUNC_GETMEDIAATTRIBINFOSINGLE	56		//��ȡý��������Ϣ,��ID��ȡ
#define FUNC_GETMEDIAATTRIBINFO			57		//��ȡý��������Ϣ,ID������ֵ��ȡ
#define FUNC_UPDATEMEDIAATTRIBTYPENAME	58		//����ý�����������
#define FUNC_GETMEDIAATTRIBTYPENAME		59		//��ȡý�����������
#define FUNC_GETMEDIAVERSION			60		//��ȡý��汾��Ϣ
#define FUNC_GETTRIGGERTABLE			61		//��ȡ��������������������͸��豸�Ĵ�����������������ID�ʹ�����MD5ֵ
#define FUNC_GETEVENTTABLE				62		//��ȡ�¼���������������͸��豸���¼��������¼�ID���¼�MD5ֵ
#define FUNC_REMOVEDEVICE				63		//ɾ���豸
#define	FUNC_STOPEXCHANGE		64		//�豸ֹͣ����
#define	FUNC_EXCHANGESEND		65		//�豸��������
#define	FUNC_GETUPDATEINFO		66		//��ȡ�����豸����汾
#define	FUNC_SETSYSTEMTIME		67		//����ϵͳʱ��
#define	FUNC_BACKUPDB			68		//���󱸷����ݿ�
#define	FUNC_GETDBDATA			69		//�������ݿ�����
#define	FUNC_RESTOREDB			70		//����ָ����ݿ�
#define	FUNC_SETDBDATA			71		//�·����ݿ�����
#define	FUNC_DBOPERSTOP			72		//���ݻָ�����ִ�д��󷵻�
#define FUNC_GETMEDIASTATUS		  73		//��ȡý��״̬��Ϣ,ID������ֵ��ȡ
#define FUNC_GETMEDIASTATUSSINGLE 74		//��ȡý��״̬��Ϣ,��ID��ȡ
#define FUNC_SETSTREAMINFO			75		//��������Ϣ
#define FUNC_GETDEVICEINDENTIFYINFO 76		//��ȡʶ������Ϣ
#define FUNC_AREASERVERSYNC			77		//���������ͬ������,�������������,�����豸��ֹʹ��
#define FUNC_AREASERVERLOGMONITOR	78		//��־����
#define FUNC_SECONDFUNCTION			79		//�����벻����, ����Э��ͷ

/*����Ϊϵͳ����������*/
#define FUNC_TICK				240					//����TICKֵ,����NTP
#define FUNC_SERVERREPORT		241					//�����������͵��鲥����
#define FUNC_GETALLDEVIPINFO	242					//��ȡ�����豸IP��Ϣ,�ػ�����ʹ��,������ֹʹ��
#define FUNC_RETURNALLDEVIPINFO	243					//���������豸IP��Ϣ,�ػ�����ʹ��,������ֹʹ��
#define FUNC_NATDEVIPINFO		244					//����ָ���豸��ĳ�豸����NAT,�ػ�����ʹ��,������ֹʹ��
#define FUNC_DEVICEIDREPEAT		245					//֪ͨID���ظ��ˣ���������ʹ�õ�ID���ڷ������ϻ�����,��������ǿ�����ߺ󣬷�������Ϣδ���£��Ժ�10������������
#define FUNC_VALIDATEDEVICEID	246					//�豸��֤������Ϣ

//����������
#define SECOND_FUNC_SYSTEMFUNCTIONLIMIT			1 //ϵͳ��������
#define SECOND_FUNC_DELALLOCATEDEVICEID			2 //ɾ��������豸ID
#define SECOND_FUNC_MODIFYALLOCATEDEVICEID		3 //�޸ķ�����豸ID

#define SERVER_ID_AREA			0xff000000	//�����������ʼ��ַ
#define SERVER_ID_MEDIA			0xfe000000	//ý���������ʼ��ַ
#define SERVER_ID_STREAM		0xfd000000	//����������ʼ��ַ
#define SERVER_ID_MASK			8

#define DB_DEVICE					1				//���ݿ����Ϊ�豸��ʽ
#define DB_USER						2				//���ݿ����Ϊ�û�����ʽ

#define DB_DATA_INT					0
#define DB_DATA_BLOB				1

#define DB_FILEDATA_MAXSIZE		40960
//���ݿⱸ�ݻָ�����������
//////////////////////////////////////////////////////////////////////////
#define DB_NOERROR		0
#define DB_OPERERROR	1	//��������
#define DB_EOF			2	//�ļ�β
#define DB_USER_ERROR	3	//�û���֤ʧ��
#define DB_BUSY			4	//���ݻָ��������û�ռ��
#define DB_NOREADY		5	//û׼����
#define DB_FILEERROR	6	//�����ļ�����ʧ��
#define DB_REBOOTERROR	7	//�������Թرճ���
#define DB_ID_ERROR		8	//ID��֤����
#define DB_DBVERHIGH	9	//���ݿ�汾�Ȼָ��Ļ����汾��
#define DB_DBDATAWRONG	10	//���ݿ��ļ����ݴ���
#define DB_TIMEOUT		11	//�û���ָ����ʱ����δִ�в�����ǿ�Ƴ�ʱ����

#define LOG_LEVEL_ERROR			0		//log��Ϣ�ļ���0��ʾ����
#define LOG_LEVEL_WARN			1		//����
#define LOG_LEVEL_INFO			2		//��ͨ��Ϣ
#define LOG_LEVEL_DEBUG			3		//������Ϣ
#define LOG_LEVEL_SYSTEM_SHUTDOWN		21		//ϵͳ�ر�
#define LOG_LEVEL_SYSTEM_START			20		//ϵͳ����
#define LOG_LEVEL_DEVICE_ONLINE			19		//�豸����
#define LOG_LEVEL_DEVICE_OFFLINE		18		//�豸����


#define DEVICE_MODE_GROUP		1		//����IDΪ��ID
#define DEVICE_MODE_DEVICE		2		//����IDΪ�豸ID
#define DEVICE_MODE_DEVIDENTIFY	3		//����IDΪ�豸ʶ����ID

#define LOGINMODE_POWERON		0		//Ϊ�ϵ��ע���豸,
#define LOGINMODE_OFFLINE		1		//Ϊ���ߺ�ע���豸

#define RETURN_ERROR_NONE			0		//���صĴ���ֵ����Ϊ�������޴���
#define RETURN_ERROR_NOTBEING		1		//������
#define RETURN_ERROR_NOTONLINE		2		//������
#define RETURN_ERROR_NOSERVER		3		//�޷��ҵ����õķ�����
#define RETURN_ERROR_DENIAL			4		//�ܾ�����
#define RETURN_ERROR_PASSWORD		5		//�������
#define RETURN_ERROR_FORMAT			6		//��ʽ����
#define RETURN_ERROR_NOPOWER		7		//��Ȩ�޲���
#define RETURN_ERROR_ISBEING		8		//�Ѵ���
#define RETURN_ERROR_UPDATEVER		9		//�����İ汾�ŵ���ϵͳ����ʹ�õİ汾��
#define RETURN_ERROR_DBOFFLINE		10		//���ݿ�û������
#define RETURN_ERROR_NOTCONNTECT	11		//δ���ӵ��������
#define RETURN_ERROR_NORES			12		//û����Դ��
#define RETURN_ERROR_TIMEOUT		13		//��ʱ
#define RETURN_ERROR_ZERO			14      //����Ϊ0
#define RETURN_ERROR_SERVERERROR    15		//�����������������
#define RETURN_ERROR_NOSPACE		16		//û���㹻�Ŀռ���
#define RETURN_ERROR_DBOPERERROR	17		//���ݲ�������
#define RETURN_ERROR_NOTIDENTITY	18		//û�б�ʶ��
#define RETURN_ERROR_MAXDEVICENUM	19		//������������豸��
#define RETURN_ERROR_MAXERRORTIMES	20		//���������������,�ܾ���½
#define RETURN_ERROR_PARSEERROR		21		//�����������֤ʧ��
#define RETURN_ERROR_PROTOCALNOMATCH	22		//Э��汾��ƥ��
#define RETURN_ERROR_EXPIRATIONTIME		23		//����������Ѿ������� expiration Time
#define RETURN_ERROR_COMMVERNOMATCH		24		//Э��汾��ƥ��,�ܾ�����
#define RETURN_ERROR_OVERRUN			25		//������Χ
#define RETURN_ERROR_NOTOPEN			26		//�޷���
#define RETURN_ERROR_FORCEDSTOP			27		//ǿ��ֹͣ
#define RETUNR_ERROR_LOGONERROR			28	    //��½����
#define RETUNR_ERROR_MAXONLINENUM		29	    //������󲢷��豸��,�ܾ���½

//ý��������������صĴ���ֵ
#define RETURN_ERROR_SEC_NONE			0			//��Ϊ�������޴���
#define RETURN_ERROR_SEC_NOTBEING		0xf001		//ý�����ͨ��������
#define RETURN_ERROR_SEC_ISBEING		0xf002		//ý�����ͨ���Ѵ���
#define RETURN_ERROR_SEC_SERVER			0xf003		//ý����������������쳣
#define RETURN_ERROR_SEC_NOTBEINGENCODE	0xf004		//���������Ҳ������õ�������
#define RETURN_ERROR_SEC_SERVERBUSY		0xf005		//ý�������������æ���ܾ�����
#define RETURN_ERROR_SEC_CHANNELBUSY	0xf006		//����������ͨ�����̫���ˣ��ܾ�����
#define RETURN_ERROR_SEC_STOP			0xf007		//δ������ֹͣ��ͨ����ý������
#define RETURN_ERROR_SEC_NOSPACE		0xf008		//û���㹻�Ŀռ���
#define RETURN_ERROR_SEC_IDENTIFYOCCUPY	0xf009		//��ʶ���ѱ�ռ��,������
#define RETURN_ERROR_SEC_SENDERROR		0xf00a		//���ݷ���ʧ��
#define RETURN_ERROR_SEC_INITIALERROR	0xf00b		//��ʼ������ʧ��

#define STREAM_MODE_DYNAMIC		0xff000000		//�Ǽ�Ϊ��̬��ַ�����Զ����䣩

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

#define MAKEMEDIATYPE(x,y,z)	 (x | y | z)			//�ϳ�ý�����
#define GETMAINTYPE(x)			 (x & MAINTYPEBITMASK)	//��ȡ�����
#define GETSUBTYPE(y)			 (y & SUBTYPEBITMASK)	//��ȡ�����
#define GETSAVETYPE(z)			 (z & SAVETYPEBITMASK)	//��ȡ�ļ��������
#define IS_RECORD_MEDIA(mediaId) ((mediaId & MAINTYPEBITMASK) == MEDIA_FILETYPE_RECORD) //�Ƿ�Ϊ¼���ļ�
#define IS_UPDATE_MEDIA(mediaId) ((mediaId & MAINTYPEBITMASK) == MEDIA_FILETYPE_UPDATE) //�Ƿ�Ϊ�����ļ�
#define IS_TEMP_MEDIA(mediaId)	 ((mediaId & SAVETYPEBITMASK) == MEDIA_FILETYPE_TEMP)   //�Ƿ�Ϊ��ʱ�ļ�

#define MAX_PASSWORDLEN			32		//������볤��
#define MAX_IP4ADDRNAMELEN		40		//IPV4��ַ��Ϣ��󳤶�
#define MAX_DNSADDRNAMELEN		64		//dns������󳤶�
#define MAX_USERNAMELEN			20		//�����û�������
#define MAX_SHORTCUTDESCRIBE	120		//��ݷ�ʽ����
#define MAX_FILENAMELEN			60		//����ļ�������
#define MAX_TRIGGERNAMELEN		80		//��󴥷���������
#define MAX_EVENTNAMELEN		80		//����¼�������
#define MAX_SHORTCUTNAMELEN		80		//��ݲ���������
#define MAX_DEVICENAMELEN		40		//����豸������
#define MAX_DEVICEGROUPNAMELEN	40		//����豸��������
#define MAX_FILEPATHLEN			256		//����ļ�·������
#define MAX_LOGLEN				1024	//����LOG����
#define MAX_STREAMNAMELEN		40		//��������Ƴ���

#define DEVICE_OFFLINE			0		//�豸���ߣ����ԼĴ������ʾ
#define DEVICE_ONLINE			1		//�豸���������ԼĴ������ʾ
#define DEVICE_HADREMOVE		0xfffffffe //0�żĴ���,�豸�Ѿ���ɾ��
#define DEVICE_ATTRIB_STATUS	0		//�豸����ֵ���ʾ�豸״̬�ļĴ�����Ϊ���߻��������豸������

#define DEVICE_ATTRIB_VERSION   5		//�豸������汾��Ӳ���汾,��16λΪ����汾,��16λΪӲ���汾

#define DEVICE_UPDATE_WORKING	1		//�豸�����У����ԼĴ������ʾ
#define DEVICE_UPDATE_ONIDLE	0		//�豸��������״̬�����ԼĴ������ʾ
#define DEVICE_UPDATE_GIVEUP	2		//�豸������,��������
#define DEVICE_ATTRIB_UPDATE    6		//�豸����������־λ

#define DEVICE_ATTRIB_SYSTEM	63		//�豸����ֵ(����ͼ������ʾϵͳ���õ�һЩ�豸����

//
#define  SECONDSERVER_ATTRIB_CPURATE	2	//cpu��ǰʹ����
#define  SECONDSERVER_ATTRIB_NETRATE	3	//��������ʹ����
#define  STREAMSERVER_ATTRIB_CHANNELS	48	//����������ǰ�������ͨ����

#define ATTRIB_BIT_ALL			0xffffffffffffffffULL		//ȫ������λ
#define ATTRIB_BIT(n)			((int64u)1<<n)					//����λ

#define USER_POWER_CONTROL		ATTRIB_BIT(0)		//��������û����ƣ��������������û�ֻ�ܲ鿴�û���������ܿ���

#define USER_POWER_LEVEL1		(1<<1)				//�༭�Լ����鿴������ȫ��
#define USER_POWER_LEVEL2		(2<<1)				//�༭����ȫ��
#define USER_POWER_MEDIATYPE	ATTRIB_BIT(3)		//����༭ý�����
#define USER_POWER_DEVICE		ATTRIB_BIT(4)		//����༭�豸
#define USER_POWER_DEVICEGROUP	ATTRIB_BIT(5)		//����༭�豸��
#define USER_POWER_USER			ATTRIB_BIT(6)		//����༭�û�
//#define USER_POWER_ALLOWREMOVEDEVICE	ATTRIB_BIT(7)		//����ɾ���豸
#define USER_POWER_ALLOWMULTI	ATTRIB_BIT(63)		//�����û�ͬʱ��¼����豸

#define USER_STYLE_NEEDVERIFY	0x1			//�Ƿ���Ҫ��ʾ�û�ȷ��

//����������Ĵ���
#define AREA_REG_DEVICEONLINECOUNTS			1 //�����豸����
#define AREA_REG__UPDATE					DEVICE_ATTRIB_UPDATE//�豸����������־λ
#define AREA_REG_REGSTATUS					2 //���������ע��������Ϣ
#define AREA_REG_SYSTEM_VERSION				10//ϵͳ������汾��Ӳ���汾,��16λΪ����汾,��16λΪӲ���汾
#define AREA_REG_EXPIRATIONTIME				11//ϵͳ��Ч��
#define AREA_REG_MEDIAMODIFYDETAILINFO		48//���� ɾ�� ���� ý���ļ�����Ϣ�����ݽṹ�μ��·�
#define AREA_REG_MEDIAATTRIBMODIFYINFO		49 //���� ɾ�� ���� ý�����Ե���Ϣ�����ݽṹ�μ��·�
#define AREA_REG_GROUP						50 //���ӡ�ɾ������������Ϣ�����ݽṹ�μ��·�
#define AREA_REG_DB							51 //���ӡ�ɾ�����������ݿ���Ϣ�����ݽṹ�μ��·�
#define AREA_REG_EVENT						52 //���ӡ�ɾ���������¼���Ϣ�����ݽṹ�μ��·�
#define AREA_REG_TRIGGER					53 //���ӡ�ɾ�������´�������Ϣ�����ݽṹ�μ��·�
#define AREA_REG_STREAM						54 //���ӡ�ɾ������������Ϣ�����ݽṹ�μ��·�
#define AREA_REG_LOGINFO					55 //������־

//AREA_REG_REGSTATUS -- ����(eg:0x00000101 ���������Ǽ��ܹ�ע������ ״ֵ̬Ϊ1��ʾ����)
#define AREASERVER_ATTRIB_REGTYPEBIT		0x0000ff00			//���������ע����������
#define AREASERVER_ATTRIB_REGTYPEVALBIT		0x000000ff			//���������ע�����Ͷ�Ӧ��ֵ������
#define AREASERVER_ATTRIB_REGTYPE_FILE	0x0		//���������ʹ�����ļ���ע�᷽ʽ
#define AREASERVER_ATTRIB_REGTYPE_UDOG	0x1		//���������ʹ���˼��ܹ���ע�᷽ʽ
#define AREASERVER_ATTRIB_REGTYPE_UDOG_OFFLINE	0x0		//���ܹ�������
#define AREASERVER_ATTRIB_REGTYPE_UDOG_ONLINE	0x1		//���ܹ�����

#define AREASERVER_ATTRIB_GROUP_ADD		0x1		//���������50�żĴ�����funcֵ����ʾ������һ���豸��
#define AREASERVER_ATTRIB_GROUP_DEL		0x2		//50�żĴ�����funcֵ����ʾɾ��һ����һ���豸��
#define AREASERVER_ATTRIB_GROUP_UPDATE	0x3		//50�żĴ�����funcֵ����ʾ������һ���豸��

#define AREASERVER_ATTRIB_MEDIA_ADD		0x11		//���������48�żĴ�����funcֵ����ʾ������һ��ý���ļ�
#define AREASERVER_ATTRIB_MEDIA_UPDATE	0x12		//48�żĴ�����funcֵ����ʾ������һ��ý���ļ�
#define AREASERVER_ATTRIB_MEDIA_DEL		0x13		//48�żĴ�����funcֵ����ʾɾ��һ����һ��ý���ļ�
#define AREASERVER_ATTRIB_MEDIA_STATUS	0x14		//48�żĴ�����funcֵ����ʾһ��ý���ļ�״̬�ı�

#define AREASERVER_ATTRIB_MEDIAATTRIB_ADD		 0x1		//���������49�żĴ�����funcֵ����ʾ������һ��ý������
#define AREASERVER_ATTRIB_MEDIAATTRIB_UPDATE	 0x2		//49�żĴ�����funcֵ����ʾ������һ��ý������
#define AREASERVER_ATTRIB_MEDIAATTRIB_DEL		 0x3		//49�żĴ�����funcֵ����ʾɾ��һ����һ��ý������
#define AREASERVER_ATTRIB_MEDIAATTRIB_NAMEUPDATE 0x4		//49�żĴ�����funcֵ����ʾ����ý�������������

#define AREASERVER_DB_CHANGE	0x11		//���������51�żĴ�����funcֵ����ʾ���ݿ������ӻ��޸���һ��
#define AREASERVER_DB_DEL		0x12		//51�żĴ�����funcֵ����ʾɾ�����ݿ��е�һ������

#define AREASERVER_EVENT_ADD	0x11		//���������52�żĴ�����funcֵ����ʾ������һ���¼�
#define AREASERVER_EVENT_UPDATE	0x12		//52�żĴ�����funcֵ����ʾ������һ���¼�
#define AREASERVER_EVENT_DEL	0x13		//52�żĴ�����funcֵ����ʾɾ��һ����һ���¼�

#define AREASERVER_TRIGGER_ADD		0x11		//���������53�żĴ�����funcֵ����ʾ������һ��������
#define AREASERVER_TRIGGER_UPDATE	0x12		//53�żĴ�����funcֵ����ʾ������һ��������
#define AREASERVER_TRIGGER_DEL		0x13		//53�żĴ�����funcֵ����ʾɾ��һ����һ��������

#define CONDITION_CHANGE	0				//��ֵ�ı�ʱ
#define CONDITION_EQUAL		1				//��ֵ����ʱ
#define CONDITION_GEQUAL	2				//��ֵ���ڵ���ʱ
#define CONDITION_LEQUAL	3				//��ֵС�ڵ���ʱ
#define CONDITION_UNTIL		10				//ʱ����ǩ���ʾһֱִ�е��ض���ʱ�䣨ʵʱʱ�ӣ�

#define PLAY_STREAM_ID_CHANNEL			0x1 //Ϊ��ͨ��
#define PLAY_STREAM_ID_DEVICE			0x2 //Ϊ�豸
#define PLAY_STREAM_ID_DEVICEIDENTIFY   0x3 //Ϊ�豸��ʶ����

#define MEDIAATTRIB_MAXATTRIB	  255
//ý������ʹ������,��ý��������� MEDIAATTRIB_TYPE_USE �� MEDIAATTRIB_LIMIT(����MEDIAATTRIB_LIMIT)����Ϊϵͳʹ�õ�ý������,�û�������ʹ�ø÷�Χ�ڵ�ֵ
#define MEDIAATTRIB_TYPE_USE	  3
#define MEDIAATTRIB_LIMIT         220
#define MEDIAATTRIB_IMPORTANTFILE 220  //��Ҫ��ý���ļ�
#define IS_IMPORTANT_MEDIAFILE(mdeiaattrib) (((mdeiaattrib >> 24) == MEDIAATTRIB_IMPORTANTFILE)? 1: 0) //�ж��Ƿ�Ϊ��Ҫ������ļ�

#define LOGONKEYINFO_MAXSIZE  128
#define MD5SELFSIZE 16	//MD5ֵ�ֽڴ�С
#define MD5SELFASCLLSIZE 32	//MD5ֵ�ֽڴ�С
//#define ENABLEMEDIAMD5ZIP  0 //ѹ��ý���ܱ�

//
#define MAXTIMES_ERRORLOGIN		5  //����½�������
#define MAXLOCKTIME_ERRORLOGIN  1//10 //����½��������ʱ��,����

//ɾ���豸������־
#define REMOVEDEVICE_FLAG_FORCEREMOVE  0x01


static const int32u g_idmask[33]=					//������
{
	0x00000000,
	0x80000000,
	0xc0000000,
	0xe0000000,
	0xf0000000,
	0xf8000000,
	0xfc000000,
	0xfe000000,
	0xff000000,
	0xff800000,
	0xffc00000,
	0xffe00000,
	0xfff00000,
	0xfff80000,
	0xfffc0000,
	0xfffe0000,
	0xffff0000,
	0xffff8000,
	0xffffc000,
	0xffffe000,
	0xfffff000,
	0xfffff800,
	0xfffffc00,
	0xfffffe00,
	0xffffff00,
	0xffffff80,
	0xffffffc0,
	0xffffffe0,
	0xfffffff0,
	0xfffffff8,
	0xfffffffc,
	0xfffffffe,
	0xffffffff
};

typedef struct  tag_SecondNetHeader 
{
	int32u		func;
}SecondNetHeader, *pSecondNetHeader;

typedef struct _tag_address
{
	int32u id;
	int8u mode;
	int8u mask;
	int8u fill[2];
}Address,*PAddress;

typedef struct _tag_updatedevicefile	//�����ļ��ṹ
{
	int32u	fileId;								//�ļ�ID��
	int16u	softVer;							//����汾��
	int16u	hardVer;							//���ֽ�Ӳ���汾�ţ����ֽڷǶ���ʶ���
	int8u	filename[MAX_FILENAMELEN];			//�ļ���
	int8u	path[MAX_FILEPATHLEN];				//�ļ�·��
}UpdateDeviceFile,*PUpdateDeviceFile;

typedef struct _tag_usershortcut		//�û���ݲ���
{
	int32u execId[4];
	int8u name[4][MAX_SHORTCUTNAMELEN];
	int8u num;							//���ж��ٸ��¼�ID������������4�����簴ť���º͵���ֱ���ʾʲô���ֺ�ִ��ʲô�¼�
	int8u style;							//���
	int8u fill[2];
	int8u describ[MAX_SHORTCUTDESCRIBE]; //�Ա���ݲ���������
}UserShortcut,*PUserShortcut;

typedef struct _tag_serveraddress_
{
	int8u addr[MAX_DNSADDRNAMELEN];				//ip��ַor����,���ַ�����ʾ��֧���������г�������
	int16u port;				//�˿ں�
	int8u fill[2];
}StuPeerAddress,*PStuPeerAddress;

typedef struct _tag_systemattrib
{
	int8u name[MAX_DEVICENAMELEN];		//�豸��
	int32u parentId;						//Ϊ0��ʾ��ָ�����豸������ǰ�豸��Ҫָ��Ϊĳ���豸�����豸ʱ���豾����Ϊ���豸ID
	int32u bindUser;						//Ϊ0��ʾ����Ĭ���û�����Ϊ����ֵ����������豸�ڴ��û�Ȩ������������¼������������
	int64u style;						//������һЩ�趨ֵ����λ��ʾ
	StuPeerAddress stuAddr[5];
	int8u mask[MAX_IP4ADDRNAMELEN];						//������
	int8u gateway[MAX_IP4ADDRNAMELEN];					//���ص�ַ
	int8u dns1[MAX_IP4ADDRNAMELEN];						//dns��ַ
	int8u dns2[MAX_IP4ADDRNAMELEN];
	int8u defaultPri;					//Ĭ�����ȼ���0-63��Ĭ��ֵΪ32�����ڸ��豸��̬�����¼��е����ȼ�
	int8u addrtype;						//��ַ���ͣ�0Ϊ��̬���䡣1Ϊ��̬���䣬stuAddr[0]�������á�2Ϊ�����á�ע�����������農̬����
	int8u defaultCallPri;				//Ĭ��Ѱ�����ȼ�
	int8u defaultTalkPri;				//Ĭ�϶Խ����ȼ�
}SystemAttrib,*PSystemAttrib;

typedef struct _tag_attrib_exchange		//�����Ĵ���
{
	int32u	deviceId;					//Ŀ���豸ID��
	int32u	sequence;					//���
	int32u	dataSize;					//���ݳ��ȣ����������ṹ��
}Attrib_Exchange,*PAttrib_Exchange;

//����ý��ͬ����Ϣ�ĸ���
#define CAL_MEDIA_SYNCINFOLEN(msec) (msec / 1000 + 2) //((msec > 0)? (msec / 1000 + 2): 0)
typedef struct _tag_atocfileinfosingle
{
	int8u  md5_self[MD5SELFSIZE];//���ṹ����Ϣ�Ѳ�����md5ֵ
	int8u  filename[MAX_FILENAMELEN];
	int32u mediaid;

	int32u creatorId;
	int32u createDeviceId;
	int32u msec; //����ʱ������msΪ��λ,���Ϊ�����ļ� ��¼�����İ汾��Ϣ���ݳ���
	int32u attrib;
	int64u len;
	int8u  md5[MD5SELFSIZE];
	int32u syncinfolen;		//ý�������Ϣ����
}AToCFileInfoSingle,*PAToCFileInfoSingle;

#define WHOLE_MEDIAFILE		 1 //������ʹ�õ��ļ�
#define PART_MEDIAFILE		 2 //���������ļ�
#define NOTEXIST_MEDIAFILE	 3 //δ��⵽ý����������ڴ��ļ�
typedef struct _tag_atocfilestatussingle
{
	int32u mediaid;
	int32u status;

	//int64u lastplaytime;
	//int32u playtimes;
}AToCFileStatusSingle,*PAToCFileStatusSingle;

typedef struct _tag_areaattrib_meida	//�����������48�żĴ����д�ŵ����ݣ���ý���ļ����ӡ����º�ɾ��ʱ�����
{
	int32u	func;						//���ܲμ�AREASERVER_ATTRIB_MEDIA_ADD�ȣ����������ΪAREASERVER_ATTRIB_MEDIA_DEL����Ҫ�ô˽ṹ��func��Ϊһ��WORD����ֵ����ʾɾ�����ݣ����±�ʾ������ļ�ID
	int64u  mediatimestamp;			//ý���޸ĵ�ʱ���
	AToCFileInfoSingle fileInfo;	//������ļ���Ϣ
	////���Ϊ����ý���ļ������Ż���ý���ͬ����Ϣ,�����ӵ�ý���ļ���ʱ���Ϊδ����,Ҫ��ý�������������������ɹ��ǼǺ�ſ���
}AreaAttrib_Media,*PAreaAttrib_Media;

typedef struct _tag_areaattrib_deviceattrib	
{
	int32u	func;

	int32u devid;
	int32u devidentifyid;
	int	   devattriblen;
}AreaAttrib_DeviceAttrib,*PAreaAttrib_DeviceAttrib;

typedef struct _tag_areaattrib_mediadel	//�����������48�żĴ����д�ŵ����ݣ���ý���ļ����ӡ����º�ɾ��ʱ�����
{
	int32u	func;						//AREASERVER_ATTRIB_MEDIA_DEL
	int64u  mediatimestamp;				//ý���޸ĵ�ʱ���
	int16u	num;
	int8u fill[2];
}AreaAttrib_MediaDel,*PAreaAttrib_MediaDel;

typedef struct _tag_areaattrib_mediastatus	//�����������48�żĴ����д�ŵ����ݣ���ý���ļ�״̬�ı�
{
	int32u	func;						//AREASERVER_ATTRIB_MEDIA_STATUS
	AToCFileStatusSingle	status;
}AreaAttrib_MediaStatus,*PAreaAttrib_MediaStatus;

typedef struct _tag_areaattrib_meidaattrib	//�����������49�żĴ����д�ŵ����ݣ���ý���������ӡ����º�ɾ��ʱ�����
{
	int32u	func;						//���ܲμ� AREASERVER_ATTRIB_MEDIAATTRIB_ADD�ȣ����������ΪAREASERVER_ATTRIB_MEDIAATTRIB_DEL����Ҫ�ô˽ṹ��func��Ϊһ��WORD����ֵ����ʾɾ�����ݣ����±�ʾ��������Եı�ʶID��
	int8u	attribname[MAX_FILENAMELEN];//��������
	int8u	mediaId;					//�����������
	int8u	attrib;						//��������µ���������
	int8u fill[2];
}AreaAttrib_MediaAttrib,*PAreaAttrib_MediaAttrib;


typedef struct _tag_areaattrib_meidaattribdel	//�����������49�żĴ����д�ŵ����ݣ���ý���������ӡ����º�ɾ��ʱ�����
{
	int32u	func;						//AREASERVER_ATTRIB_MEDIAATTRIB_DEL
	int16u	num;
	int8u fill[2];
}AreaAttrib_MediaAttribDel,*PAreaAttrib_MediaAttribDel;

typedef struct _tag_areaattrib_group	//�����������50�żĴ����д�ŵ����ݣ����豸���������ӡ����º�ɾ��ʱ�����
{
	int8u func;							//���� ɾ�� ����
	int8u _fill;
	int16u deviceNum; //(����,����:���ڵĵ�ַ��������Ϊ Address,��ʾ������ĵ�ַ��Ϣ),(ɾ��:���ڵĵ�ַ��������Ϊ int32u,��ʾ��ID)
	int32u groupId;
	int32u creatorId;
	int32u createDeviceId;
	int8u name[MAX_DEVICEGROUPNAMELEN];
}AreaAttrib_Group,*PAreaAttrib_Group;


typedef struct _tag_areaattrib_db_change		//�����������51�żĴ����д�ŵ����ݣ����ݿ������ݸĶ�
{
	int8u		func;					//�Ķ�
	int8u		mode;					//ģʽ��DB_DEVICE,DB_USER
	int8u		isBlob;					//0=int,1=blob
	int8u		_fill;
	int32u		valueid;				//mode=DB_DEVICE,�豸ID,mode=1,�û�ID
	int32u		paramId;				//����ID��
	int32u		val;					//isBlob=1,�����ܳ���,isBlob=0,����ֵ
}AreaAttrib_DB_Change, *PAreaAttrib_DB_Change;

typedef struct _tag_areaattrib_db_del		//�����������51�żĴ����д�ŵ����ݣ����ݿ������ݱ�ɾ��
{
	int8u		func;					//ɾ��
	int8u		mode;					//ģʽ��DB_DEVICE,DB_USER
	int8u		isBlob;					//0=int,1=blob
	int8u		_fill;
	int32u		valueid;				//mode=DB_DEVICE,�豸ID,mode=1,�û�ID
	int32u		paramNum;				//��������
}AreaAttrib_DB_Del, *PAreaAttrib_DB_Del;

typedef struct _tag_areaattrib_event_change			//�����������52�żĴ����д�ŵ����ݣ����¼�����ӻ����
{
	int8u		func;					//���� ����
	int8u		_fill[3];
	int32u		len;					//�¼����ֽ���
}AreaAttrib_Event_Change, *PAreaAttrib_Event_Change;

typedef struct _tag_areaattrib_event_del			//�����������52�żĴ����д�ŵ����ݣ����¼���ɾ��
{
	int8u		func;					//ɾ��
	int8u		_fill[3];
	int32u		num;					//�����ٸ�ID
}AreaAttrib_Event_Del, *PAreaAttrib_Event_Del;

typedef struct _tag_areaattrib_trigger_change			//�����������53�żĴ����д�ŵ����ݣ��д���������ӻ����
{
	int8u		func;					//���� ����
	int8u		_fill[3];
	int32u		len;					//���������ֽ���
}AreaAttrib_Trigger_Change, *PAreaAttrib_Trigger_Change;

typedef struct _tag_areaattrib_trigger_del			//�����������53�żĴ����д�ŵ����ݣ��д�������ɾ��
{
	int8u		func;					//ɾ��
	int8u		_fill[3];
	int32u		num;					//�����ٸ�ID
}AreaAttrib_Trigger_Del, *PAreaAttrib_Trigger_Del;


//////////////////////////////////////////////////////////////////////////
#define AREASERVER_STREAM_ADD		0x1		//���������54�żĴ�����funcֵ����ʾ�豸����,�Ǽǵ�����Ϣ,���ݸ�ʽΪ StreamInfo ����
#define AREASERVER_STREAM_UPDATE	0x2		//53�żĴ�����funcֵ����ʾ��������Ϣ,���ݸ�ʽΪ LittleStreamInfo ����
#define AREASERVER_STREAM_DEL		0x3		//53�żĴ�����funcֵ����ʾɾ����Ϣ,���ݸ�ʽΪɾ������ID ����

//stream subtype
#define STREAM_SUBTYPE_UNKNOW			   0 //
#define STREAM_SUBTYPE_AUDIO_LOOPBACK	   1 //��Ƶ��·�ɼ�
#define STREAM_SUBTYPE_AUDIO_CAPTURE	   2 //�����ɼ�
#define STREAM_SUBTYPE_SCREEN		       3 //��Ļ�ɼ�
#define STREAM_SUBTYPE_CAMARA			   4 //����Բɼ�
#define STREAM_SUBTYPE_IPC_HK			   5 //������������ͷ
#define STREAM_SUBTYPE_MIX_LOOPBACK_SCREEN 6 //�����
#define STREAM_SUBTYPE_MIX_LOOPBACK_CAMARA 7 //�����
#define STREAM_SUBTYPE_MIX_CAPTURE_SCREEN  8 //�����
#define STREAM_SUBTYPE_MIX_CAPTURE_CAMARA  9 //�����
#define STREAM_SUBTYPE_END				   10
typedef struct  tag_StreamInfo
{
	int32u deviceid;
	int32u streamid;
	int8u  streamindex;
	int8u  subtype;
	int8u  fill[2];
	int8u  streamname[MAX_STREAMNAMELEN];
}StreamInfo, *pStreamInfo;

typedef struct  tag_LittleStreamInfo
{
	int32u streamid;
	int8u  streamname[MAX_STREAMNAMELEN];
	int8u  subtype;					//
	int8u  _fill[3];
}LittleStreamInfo, *pLittleStreamInfo;

typedef struct _tag_areaattrib_stream_change
{
	int8u		func;					//
	int8u		_fill[3];
	int32u		num;
}areaattrib_stream_change, *pareaattrib_stream_change;

typedef struct _tag_areaattrib_loginfo	
{
	int32u  deviceid;
	int8u   level;
	int8u   _fill;
	int16u   loglen;
}AreaAttrib_LogInfo,*PAreaAttrib_LogInfo;

typedef struct _tag_area_userchange
{
	int32u		countDstId;				//Ŀ��ID����ŵ�����
	int32u		createid;				//
	int32u		userid;
	int8u		name[MAX_USERNAMELEN];	//�û���
	int8u		password[MAX_PASSWORDLEN];//�û����루MD5)
	int64u		power;					//�û�Ȩ�ޣ��ο�USER_POWER_
	int16u		triggerNum;				//���û��󶨵Ĵ�����ID����
	int8u		shortcutNum;			//��ݲ�������
	int8u		defaultPri;				//Ĭ�����ȼ�
	int8u		defaultCallPri;			//Ĭ��Ѱ�����ȼ�
	int8u		defaultTalkPri;			//Ĭ�϶Խ����ȼ�
	int8u		fill[2];
}Area_UserChange,*PArea_UserChange;

typedef struct _tag_return				//����ֵ
{
	int16u err;
	int32u val1;
	int32u val2;
}StuReturn,*PStuReturn;

/***********************************************************
�豸ID�ţ��豸��Ψһ��ʶ�ţ�32λ��31-26λ��������ţ�25-18λ��С����ţ�17-0λ���豸��
		��������0XFFF80000/14��Ϊ�����������0XFFF40000/14��Ϊý���������0XFFF00000/14��Ϊ��������

��ͨ��ID�ţ�ʵʱ��ͨ����ţ�ÿһ��ʵʱ��Դ��Ӧ��һ����ţ�һ���豸������ӵ��32����ͨ����ͨ����Ϊ�̶���̬����
		ͨ����32λ��31-16λΪ����ţ�15-5λϵͳ�ڱ�ţ�5-0λΪ�豸����ͨ����
		�磬ϵͳ�����豸������豸ͨ�����Ϊ0xa0000020��27λ��������豸��16��ͨ����
		��ô���豸����ͨ�ĵ���Ϊ0xa0000020-0xa000002f

ý���ļ�ID�ţ�ÿһ��ý���ļ���һ��ϵͳ�ڶ���Ψһ��ID�ţ�ID��Ϊ32λ�������3λΪ����ţ���0Ϊ��Ƶ�ļ���1Ϊ��Ƶ�ļ������ֽڵ���λΪ�ļ����ͣ���PCM��MP3
		0xe0000000/3Ϊϵͳ�����ļ�

�豸���ԣ�64���Ĵ�����0,63�żĴ���Ϊϵͳ������ֻ���������Ĵ�����������������޸ģ���0-47Ϊ32λ��ֵ��48-63�żĴ���Ϊ����ͼֵ���Ĵ����д������ƫ������
		0�żĴ���Ϊ�豸״̬���Ǽ�PUSH��ѯ�˼Ĵ������Ի�֪����豸��������״̬��63�żĴ������ϵͳ�豸��Ӧ��ϵͳ������Ϣ�����豸���ȣ�
		Լ����48�żĴ�������豸���ٱ䶯��״ֵ̬
		PUSH��ʽ�Ǽ�Ҫ��ѯ�ļĴ���ֵ���ڼĴ��������ı�ʱ������������Ὣ�����Ե�����״̬��������ѯ����
************************************************************/


#pragma pack(pop)

#endif
