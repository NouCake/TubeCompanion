module.exports = {
    //PACKET TYPES
    LOGIN:                      0,
    LOGIN_RESPONSE:             1,
    META_DATA:                  2,
    PENDING_DOWNLOAD_REQUESTS:  4,
    REQUEST:                    6,
    FILE:                       7,
    
    YT: 3,

    //LOGIN TYPES
    LOGIN_DEVICE:               100,
    LOGIN_CLIENT:               101,
    LOGIN_SUCCESS:              102,
    LOGIN_FAILED:               103,
    LOGIN_FAILED_ACTIV_CONNECTION:
                                104,
    LOGIN_FAILED_BAD_PACKET:    105,
    LOGIN_FAILED_UNKNOWN_USER:  106,
    LOGIN_FAILED_WRONG_PASSWORD:107,

    //REQUEST TYPES
    REQUEST_PENDING:            200,
    REQUEST_META:				201,
    REQUEST_COVER:				202,
    REQUEST_AUDIO:				203,

    //FILE TYPES
    FILE_IMAGE:                 300,
    FILE_AUDIO:                 301,
    FILE_VIDEO:                 302,

    //MISC
    DEFAULT_VALUE: -8555 //Random magic number
}