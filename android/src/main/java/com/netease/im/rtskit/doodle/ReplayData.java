package com.netease.im.rtskit.doodle;

import java.util.List;

public class ReplayData {


    private long mDuration;
    private List<ReplayTransaction> mReplayList;

    public ReplayData(long duration, List<ReplayTransaction> replayList) {
        mDuration = duration;
        mReplayList = replayList;
    }

    public long getDuration() {
        return mDuration;
    }

    public List<ReplayTransaction> getReplayList() {
        return mReplayList;
    }

    public static String cmds = "" +
            "0\n" +
            "1:0.072438,0.174074;5:1,0;\n" +
            "47\n" +
            "2:0.085395,0.258446;2:0.085870,0.286388;2:0.087560,0.310295;5:2,0;\n" +
            "79\n" +
            "2:0.088339,0.327910;2:0.088339,0.344611;5:3,0;\n" +
            "114\n" +
            "2:0.088339,0.359075;2:0.088339,0.366666;5:4,0;\n" +
            "146\n" +
            "2:0.088339,0.372411;2:0.088339,0.375853;5:5,0;\n" +
            "179\n" +
            "3:0.088339,0.375853;5:6,0;\n" +
            "602\n" +
            "1:0.151943,0.178704;5:7,0;\n" +
            "635\n" +
            "2:0.182605,0.168514;2:0.197255,0.167593;5:8,0;\n" +
            "667\n" +
            "2:0.206722,0.169215;2:0.213892,0.170961;5:9,0;\n" +
            "700\n" +
            "2:0.219278,0.173518;2:0.223919,0.177414;5:10,0;\n" +
            "732\n" +
            "2:0.228427,0.186190;2:0.228470,0.204238;5:11,0;\n" +
            "765\n" +
            "2:0.222314,0.235903;2:0.211055,0.265430;5:12,0;\n" +
            "797\n" +
            "2:0.194648,0.298844;2:0.184908,0.320401;5:13,0;\n" +
            "829\n" +
            "2:0.179523,0.333378;2:0.176404,0.338611;5:14,0;\n" +
            "861\n" +
            "2:0.174896,0.340766;5:15,0;\n" +
            "894\n" +
            "2:0.174046,0.340741;5:16,0;\n" +
            "926\n" +
            "2:0.190193,0.336909;2:0.199953,0.332750;5:17,0;\n" +
            "958\n" +
            "2:0.204725,0.330363;2:0.208100,0.329630;5:18,0;\n" +
            "991\n" +
            "2:0.210189,0.327823;2:0.213986,0.327778;5:19,0;\n" +
            "1023\n" +
            "2:0.220848,0.327778;3:0.220848,0.327778;5:20,0;\n" +
            "1354\n" +
            "1:0.332744,0.176852;5:21,0;\n" +
            "1386\n" +
            "2:0.362689,0.173148;2:0.370657,0.173148;5:22,0;\n" +
            "1419\n" +
            "2:0.375887,0.174307;2:0.377760,0.174074;5:23,0;\n" +
            "1452\n" +
            "2:0.379313,0.176920;2:0.373689,0.202788;5:24,0;\n" +
            "1485\n" +
            "2:0.359335,0.231906;2:0.348583,0.253723;5:25,0;\n" +
            "1519\n" +
            "2:0.341829,0.269077;2:0.338829,0.277163;5:26,0;\n" +
            "1554\n" +
            "2:0.337316,0.283289;2:0.336214,0.286211;5:27,0;\n" +
            "1586\n" +
            "2:0.336827,0.288764;2:0.343531,0.293051;5:28,0;\n" +
            "1617\n" +
            "2:0.355504,0.295370;2:0.365663,0.295370;5:29,0;\n" +
            "1650\n" +
            "2:0.370628,0.295370;2:0.373071,0.295370;5:30,0;\n" +
            "1684\n" +
            "2:0.374057,0.295370;2:0.375727,0.298134;5:31,0;\n" +
            "1717\n" +
            "2:0.373706,0.316983;2:0.366216,0.337874;5:32,0;\n" +
            "1752\n" +
            "2:0.360265,0.352226;2:0.356795,0.359767;5:33,0;\n" +
            "1783\n" +
            "2:0.354272,0.366360;2:0.353264,0.369735;5:34,0;\n" +
            "1815\n" +
            "2:0.352768,0.371267;3:0.352768,0.371267;5:35,0;\n" +
            "2628\n" +
            "1:0.054770,0.472222;5:36,0;\n" +
            "2661\n" +
            "2:0.050648,0.507407;2:0.049604,0.514481;5:37,0;\n" +
            "2694\n" +
            "2:0.048354,0.532452;2:0.047703,0.544686;5:38,0;\n" +
            "2727\n" +
            "2:0.047499,0.557331;2:0.047703,0.563326;5:39,0;\n" +
            "2760\n" +
            "2:0.048480,0.568478;2:0.051588,0.573700;5:40,0;\n" +
            "2794\n" +
            "2:0.056739,0.579820;2:0.066105,0.584104;5:41,0;\n" +
            "2826\n" +
            "2:0.078048,0.584977;2:0.088178,0.585185;5:42,0;\n" +
            "2859\n" +
            "2:0.096792,0.583871;2:0.104767,0.583333;5:43,0;\n" +
            "2906\n" +
            "2:0.110758,0.581284;2:0.114893,0.579603;2:0.116019,0.578704;3:0.116019,0.578704;5:44,0;\n" +
            "3149\n" +
            "1:0.101885,0.470370;5:45,0;\n" +
            "3181\n" +
            "2:0.093409,0.539773;2:0.087901,0.577287;5:46,0;\n" +
            "3212\n" +
            "2:0.082155,0.612270;2:0.078815,0.637673;5:47,0;\n" +
            "3243\n" +
            "2:0.076174,0.655512;2:0.073409,0.666717;5:48,0;\n" +
            "3276\n" +
            "2:0.072418,0.672319;2:0.072438,0.673986;3:0.072438,0.673986;5:49,0;\n" +
            "3549\n" +
            "1:0.202591,0.428704;5:50,0;\n" +
            "3581\n" +
            "2:0.181979,0.482870;2:0.179084,0.492999;5:51,0;\n" +
            "3614\n" +
            "2:0.176377,0.502109;2:0.175452,0.507635;5:52,0;\n" +
            "3645\n" +
            "2:0.175501,0.512035;2:0.177057,0.517857;5:53,0;\n" +
            "3677\n" +
            "2:0.183681,0.527327;2:0.196369,0.537509;5:54,0;\n" +
            "3709\n" +
            "2:0.207269,0.543688;2:0.217445,0.550102;5:55,0;\n" +
            "3741\n" +
            "2:0.224282,0.555015;2:0.229779,0.561264;5:56,0;\n" +
            "3772\n" +
            "2:0.232499,0.569767;2:0.232343,0.588758;5:57,0;\n" +
            "3804\n" +
            "2:0.227284,0.614802;2:0.218808,0.637581;5:58,0;\n" +
            "3836\n" +
            "2:0.204741,0.662074;2:0.193978,0.673225;5:59,0;\n" +
            "3867\n" +
            "2:0.181881,0.677803;3:0.181881,0.677803;5:60,0;\n" +
            "4112\n" +
            "1:0.213781,0.436111;5:61,0;\n" +
            "4144\n" +
            "2:0.245557,0.446739;2:0.252222,0.450204;5:62,0;\n" +
            "4175\n" +
            "2:0.257951,0.451852;3:0.257951,0.451852;5:63,0;\n" +
            "4536\n" +
            "1:0.363369,0.407407;5:64,0;\n" +
            "4568\n" +
            "2:0.351376,0.453788;2:0.346470,0.490091;5:65,0;\n" +
            "4600\n" +
            "2:0.341380,0.519705;2:0.337629,0.551327;5:66,0;\n" +
            "4634\n" +
            "2:0.335423,0.574648;2:0.333922,0.599039;5:67,0;\n" +
            "4667\n" +
            "2:0.334794,0.615594;2:0.336692,0.629458;5:68,0;\n" +
            "4698\n" +
            "2:0.338888,0.636912;2:0.342252,0.645548;5:69,0;\n" +
            "4730\n" +
            "2:0.347334,0.652638;2:0.354388,0.656316;5:70,0;\n" +
            "4765\n" +
            "2:0.362499,0.656129;2:0.372583,0.650866;5:71,0;\n" +
            "4798\n" +
            "2:0.380332,0.644416;2:0.391762,0.632723;5:72,0;\n" +
            "4832\n" +
            "2:0.398586,0.625398;2:0.404603,0.614797;5:73,0;\n" +
            "4864\n" +
            "2:0.409136,0.605163;2:0.410892,0.599639;5:74,0;\n" +
            "4897\n" +
            "2:0.411989,0.595263;2:0.412839,0.590436;5:75,0;\n" +
            "4930\n" +
            "2:0.410585,0.579441;2:0.398577,0.555944;5:76,0;\n" +
            "4965\n" +
            "2:0.393963,0.549026;2:0.389572,0.543050;5:77,0;\n" +
            "4998\n" +
            "2:0.384780,0.537790;2:0.379243,0.533131;5:78,0;\n" +
            "5032\n" +
            "2:0.374197,0.531078;2:0.368483,0.530556;5:79,0;\n" +
            "5063\n" +
            "2:0.361007,0.535719;2:0.353692,0.543784;5:80,0;\n" +
            "5096\n" +
            "2:0.348885,0.549718;2:0.343410,0.557511;5:81,0;\n" +
            "5129\n" +
            "2:0.337850,0.564902;2:0.336278,0.566667;3:0.336278,0.566667;5:82,0;\n" +
            "8194\n" +
            "1:0.065960,0.793519;2:0.098398,0.778685;5:83,0;\n" +
            "8226\n" +
            "2:0.113842,0.772803;2:0.122683,0.768421;5:84,0;\n" +
            "8259\n" +
            "2:0.127518,0.766777;2:0.130854,0.766667;5:85,0;\n" +
            "8291\n" +
            "2:0.133910,0.769245;2:0.138217,0.782230;5:86,0;\n" +
            "8324\n" +
            "2:0.137093,0.810070;2:0.133429,0.849448;5:87,0;\n" +
            "8357\n" +
            "2:0.127696,0.884052;2:0.123826,0.906460;5:88,0;\n" +
            "8392\n" +
            "2:0.122038,0.919148;2:0.120944,0.926769;5:89,0;\n" +
            "8423\n" +
            "2:0.119915,0.930342;3:0.119915,0.930342;5:90,0;\n" +
            "8785\n" +
            "1:0.198469,0.801852;5:91,0;\n" +
            "8820\n" +
            "2:0.219055,0.758374;2:0.234052,0.741086;5:92,0;\n" +
            "8853\n" +
            "2:0.245119,0.730550;2:0.253860,0.722197;5:93,0;\n" +
            "8887\n" +
            "2:0.258930,0.718005;2:0.262166,0.716309;5:94,0;\n" +
            "8919\n" +
            "2:0.263518,0.714605;2:0.264472,0.714815;5:95,0;\n" +
            "8953\n" +
            "2:0.267353,0.721218;2:0.268046,0.735726;5:96,0;\n" +
            "8986\n" +
            "2:0.263581,0.762629;2:0.255076,0.793665;5:97,0;\n" +
            "9018\n" +
            "2:0.240901,0.828171;2:0.232824,0.853788;5:98,0;\n" +
            "9051\n" +
            "2:0.228279,0.872358;2:0.225568,0.886013;5:99,0;\n" +
            "9083\n" +
            "2:0.224453,0.893994;2:0.224382,0.900750;5:100,0;\n" +
            "9118\n" +
            "2:0.224736,0.906859;2:0.226634,0.912257;5:101,0;\n" +
            "9152\n" +
            "2:0.230281,0.916212;2:0.239312,0.915383;5:102,0;\n" +
            "9184\n" +
            "2:0.248467,0.906556;2:0.255838,0.888868;5:103,0;\n" +
            "9217\n" +
            "2:0.258334,0.873602;2:0.258780,0.851936;5:104,0;\n" +
            "9250\n" +
            "2:0.256832,0.838751;2:0.255414,0.830058;5:105,0;\n" +
            "9282\n" +
            "2:0.251724,0.823366;2:0.247311,0.817546;5:106,0;\n" +
            "9317\n" +
            "2:0.241732,0.813133;2:0.236295,0.809714;5:107,0;\n" +
            "9350\n" +
            "2:0.230310,0.807247;2:0.225867,0.806025;5:108,0;\n" +
            "9382\n" +
            "2:0.222725,0.804379;2:0.221326,0.804630;5:109,0;\n" +
            "9414\n" +
            "2:0.219623,0.803667;2:0.217903,0.802778;3:0.217903,0.802778;5:110,0;\n" +
            "9866\n" +
            "1:0.391048,0.764815;5:111,0;\n" +
            "9897\n" +
            "2:0.359413,0.751852;2:0.344038,0.755944;5:112,0;\n" +
            "9929\n" +
            "2:0.330839,0.763998;2:0.322573,0.771463;5:113,0;\n" +
            "9962\n" +
            "2:0.317760,0.778188;2:0.314837,0.785687;5:114,0;\n" +
            "9995\n" +
            "2:0.314488,0.791087;2:0.316227,0.797164;5:115,0;\n" +
            "10029\n" +
            "2:0.323675,0.801564;2:0.340799,0.794823;5:116,0;\n" +
            "10061\n" +
            "2:0.361697,0.777873;2:0.376387,0.765047;5:117,0;\n" +
            "10094\n" +
            "2:0.385875,0.758347;2:0.390782,0.755429;5:118,0;\n" +
            "10127\n" +
            "2:0.393428,0.754630;2:0.395087,0.753704;5:119,0;\n" +
            "10160\n" +
            "2:0.397618,0.757955;2:0.397415,0.779404;5:120,0;\n" +
            "10194\n" +
            "2:0.390385,0.810222;2:0.377653,0.861492;5:121,0;\n" +
            "10227\n" +
            "2:0.365263,0.896656;2:0.358654,0.920380;5:122,0;\n" +
            "10259\n" +
            "2:0.355849,0.932797;2:0.354683,0.938188;5:123,0;\n" +
            "10291\n" +
            "3:0.354683,0.938188;5:124,0;\n" +
            "13309\n" +
            "10:https://img3.doubanio.com/view/subject/s/public/s33595280.jpg,05:125,0;\n" +
            "17337\n" +
            "1:0.120730,0.195370;5:126,0;\n" +
            "17368\n" +
            "2:0.129153,0.270396;2:0.136407,0.333420;5:127,0;\n" +
            "17399\n" +
            "2:0.141948,0.382546;2:0.145279,0.420653;5:128,0;\n" +
            "17430\n" +
            "2:0.146643,0.446726;2:0.147821,0.479730;5:129,0;\n" +
            "17461\n" +
            "2:0.148631,0.497919;2:0.149168,0.507815;5:130,0;\n" +
            "17491\n" +
            "2:0.149618,0.509306;3:0.149618,0.509306;5:131,0;\n" +
            "17823\n" +
            "1:0.131920,0.173148;2:0.171844,0.162183;5:132,0;\n" +
            "17855\n" +
            "2:0.197224,0.151647;2:0.220325,0.140292;5:133,0;\n" +
            "17888\n" +
            "2:0.242857,0.129357;2:0.264063,0.121392;5:134,0;\n" +
            "17921\n" +
            "2:0.282617,0.118063;2:0.295370,0.117593;5:135,0;\n" +
            "17955\n" +
            "2:0.309358,0.124845;2:0.320908,0.135741;5:136,0;\n" +
            "17988\n" +
            "2:0.329579,0.149610;2:0.337911,0.174417;5:137,0;\n" +
            "18020\n" +
            "2:0.343741,0.203413;2:0.347835,0.240735;5:138,0;\n" +
            "18052\n" +
            "2:0.351127,0.275638;2:0.351747,0.317647;5:139,0;\n" +
            "18087\n" +
            "2:0.352854,0.348876;2:0.352768,0.377715;5:140,0;\n" +
            "18119\n" +
            "2:0.353855,0.401696;2:0.353946,0.421972;5:141,0;\n" +
            "18154\n" +
            "2:0.353946,0.437399;2:0.354751,0.450872;5:142,0;\n" +
            "18187\n" +
            "2:0.355281,0.473148;2:0.355713,0.491022;5:143,0;\n" +
            "18219\n" +
            "2:0.356877,0.504503;2:0.357382,0.512351;5:144,0;\n" +
            "18250\n" +
            "2:0.358068,0.517028;3:0.358068,0.517028;5:145,0;\n" +
            "18642\n" +
            "1:0.233216,0.168519;2:0.205102,0.238366;5:146,0;\n" +
            "18673\n" +
            "2:0.194086,0.264207;2:0.187547,0.284543;5:147,0;\n" +
            "18705\n" +
            "2:0.184400,0.294187;2:0.182698,0.299384;5:148,0;\n" +
            "18735\n" +
            "2:0.181684,0.302778;3:0.181684,0.302778;5:149,0;\n" +
            "18977\n" +
            "1:0.242638,0.211111;5:150,0;\n" +
            "19007\n" +
            "2:0.263961,0.206761;5:151,0;\n" +
            "19038\n" +
            "2:0.265174,0.207530;2:0.266794,0.213965;5:152,0;\n" +
            "19069\n" +
            "2:0.262043,0.239282;2:0.252159,0.266461;5:153,0;\n" +
            "19101\n" +
            "2:0.238364,0.294511;2:0.227460,0.313189;5:154,0;\n" +
            "19132\n" +
            "2:0.218030,0.328505;2:0.210688,0.334922;5:155,0;\n" +
            "19163\n" +
            "2:0.202407,0.340865;2:0.199058,0.341667;3:0.199058,0.341667;5:156,0;\n" +
            "19347\n" +
            "1:0.200236,0.277778;2:0.249346,0.289887;5:157,0;\n" +
            "19378\n" +
            "2:0.265933,0.298983;2:0.279627,0.308707;5:158,0;\n" +
            "19411\n" +
            "2:0.288823,0.315473;2:0.296532,0.320706;5:159,0;\n" +
            "19445\n" +
            "2:0.303455,0.325888;2:0.307420,0.329630;3:0.307420,0.329630;5:160,0;\n" +
            "19716\n" +
            "1:0.267373,0.374074;5:161,0;\n" +
            "19747\n" +
            "2:0.284050,0.404923;2:0.285623,0.408312;5:162,0;\n" +
            "19777\n" +
            "2:0.286711,0.410032;3:0.286711,0.410032;5:163,0;\n" +
            "19993\n" +
            "1:0.258539,0.449074;2:0.277974,0.479904;5:164,0;\n" +
            "20023\n" +
            "2:0.276796,0.487037;3:0.276796,0.487037;5:165,0;\n" +
            "20264\n" +
            "1:0.185512,0.489815;5:166,0;\n" +
            "20295\n" +
            "2:0.237338,0.498611;2:0.259097,0.505426;5:167,0;\n" +
            "20326\n" +
            "2:0.283767,0.507905;2:0.301199,0.505482;5:168,0;\n" +
            "20358\n" +
            "2:0.321969,0.502810;2:0.336876,0.499499;5:169,0;\n" +
            "20389\n" +
            "2:0.355384,0.494740;2:0.356890,0.494444;3:0.356890,0.494444;5:170,0;\n" +
            "23996\n" +
            "7:0.000000,0.000000;5:171,0;\n" +
            "28179\n" +
            "11:https://ppt2h5-1259648581.file.myqcloud.com/g1o54ufrpr7q4madf7qb/index.html,05:172,0;\n" +
            "30236\n" +
            "1:0.217903,0.123148;2:0.191272,0.253546;5:173,0;\n" +
            "30269\n" +
            "2:0.186788,0.311376;2:0.186613,0.356119;5:174,0;\n" +
            "30300\n" +
            "2:0.191401,0.386770;2:0.204159,0.408802;5:175,0;\n" +
            "30334\n" +
            "2:0.227177,0.425150;2:0.255694,0.429921;5:176,0;\n" +
            "30367\n" +
            "2:0.284063,0.425462;2:0.308685,0.418342;5:177,0;\n" +
            "30400\n" +
            "2:0.328988,0.410525;2:0.344388,0.402920;5:178,0;\n" +
            "30433\n" +
            "2:0.357384,0.393618;2:0.367454,0.377422;5:179,0;\n" +
            "30465\n" +
            "2:0.369258,0.372222;3:0.369258,0.372222;5:180,0;\n" +
            "30650\n" +
            "1:0.310365,0.176852;2:0.290520,0.292131;5:181,0;\n" +
            "30682\n" +
            "2:0.285255,0.353032;2:0.282114,0.402805;5:182,0;\n" +
            "30716\n" +
            "2:0.279441,0.446618;2:0.277264,0.485588;5:183,0;\n" +
            "30749\n" +
            "2:0.275632,0.512670;2:0.279849,0.528420;5:184,0;\n" +
            "30781\n" +
            "2:0.283863,0.536111;3:0.283863,0.536111;5:185,0;\n" +
            "31052\n" +
            "1:0.501178,0.141667;2:0.478442,0.209941;5:186,0;\n" +
            "31083\n" +
            "2:0.463162,0.269388;2:0.451365,0.341122;5:187,0;\n" +
            "31113\n" +
            "2:0.443965,0.398308;2:0.440700,0.440998;5:188,0;\n" +
            "31146\n" +
            "2:0.441773,0.485065;2:0.444683,0.511115;5:189,0;\n" +
            "31179\n" +
            "2:0.450475,0.532654;2:0.456275,0.543152;5:190,0;\n" +
            "31212\n" +
            "2:0.463400,0.548114;2:0.478502,0.541832;5:191,0;\n" +
            "31245\n" +
            "2:0.493618,0.522997;2:0.506415,0.494220;5:192,0;\n" +
            "31278\n" +
            "2:0.516030,0.465667;2:0.522460,0.433037;5:193,0;\n" +
            "31314\n" +
            "2:0.522968,0.414448;2:0.521791,0.404634;5:194,0;\n" +
            "31345\n" +
            "2:0.519182,0.398852;2:0.509902,0.393813;5:195,0;\n" +
            "31377\n" +
            "2:0.493269,0.396499;2:0.477079,0.404735;5:196,0;\n" +
            "31411\n" +
            "2:0.467669,0.414768;2:0.461427,0.425460;5:197,0;\n" +
            "31442\n" +
            "2:0.459953,0.426852;5:198,0;\n" +
            "31473\n" +
            "2:0.461131,0.416667;3:0.461131,0.416667;5:199,0;\n" +
            "31655\n" +
            "1:0.690224,0.150926;5:200,0;\n" +
            "31687\n" +
            "2:0.654073,0.225534;5:201,0;\n" +
            "31718\n" +
            "2:0.637923,0.268162;2:0.626183,0.316875;5:202,0;\n" +
            "31749\n" +
            "2:0.618030,0.367433;2:0.612906,0.405326;5:203,0;\n" +
            "31779\n" +
            "2:0.613817,0.436962;2:0.616265,0.468060;5:204,0;\n" +
            "31811\n" +
            "2:0.619531,0.486937;2:0.622829,0.493905;5:205,0;\n" +
            "31843\n" +
            "2:0.628964,0.500125;2:0.638821,0.501333;5:206,0;\n" +
            "31874\n" +
            "2:0.654256,0.491887;2:0.671820,0.468098;5:207,0;\n" +
            "31909\n" +
            "2:0.689350,0.429983;2:0.698408,0.394717;5:208,0;\n" +
            "31941\n" +
            "2:0.701840,0.370849;2:0.702002,0.360323;5:209,0;\n" +
            "31974\n" +
            "2:0.700126,0.353026;2:0.690963,0.344563;5:210,0;\n" +
            "32007\n" +
            "2:0.668983,0.346743;2:0.644095,0.360306;5:211,0;\n" +
            "32040\n" +
            "2:0.619264,0.383046;2:0.602368,0.397768;5:212,0;\n" +
            "32074\n" +
            "2:0.593447,0.404936;2:0.592462,0.405556;3:0.592462,0.405556;5:213,0;\n" +
            "35084\n" +
            "13:1,05:214,0;\n" +
            "38512\n" +
            "1:0.096584,0.225000;2:0.156366,0.189971;5:215,0;\n" +
            "38544\n" +
            "2:0.173458,0.185236;2:0.194047,0.185108;5:216,0;\n" +
            "38577\n" +
            "2:0.204809,0.188199;2:0.212065,0.199016;5:217,0;\n" +
            "38611\n" +
            "2:0.214049,0.230138;2:0.204396,0.284060;5:218,0;\n" +
            "38643\n" +
            "2:0.195267,0.328991;2:0.189458,0.370907;5:219,0;\n" +
            "38676\n" +
            "2:0.187901,0.393868;2:0.187868,0.407144;5:220,0;\n" +
            "38708\n" +
            "2:0.187868,0.409259;3:0.187868,0.409259;5:221,0;\n" +
            "38949\n" +
            "1:0.293875,0.211111;5:222,0;\n" +
            "38980\n" +
            "2:0.339967,0.188617;2:0.365126,0.180222;5:223,0;\n" +
            "39011\n" +
            "2:0.386196,0.176608;2:0.404043,0.177782;5:224,0;\n" +
            "39042\n" +
            "2:0.414546,0.183693;2:0.421359,0.197803;5:225,0;\n" +
            "39074\n" +
            "2:0.422920,0.236628;2:0.417229,0.283220;5:226,0;\n" +
            "39109\n" +
            "2:0.409453,0.332401;2:0.404708,0.366319;5:227,0;\n" +
            "39140\n" +
            "2:0.404594,0.389521;2:0.407396,0.404128;5:228,0;\n" +
            "39173\n" +
            "2:0.410442,0.408764;2:0.411072,0.409259;3:0.411072,0.409259;5:229,0;\n" +
            "39444\n" +
            "1:0.590106,0.125000;2:0.632901,0.133134;5:230,0;\n" +
            "39475\n" +
            "2:0.645502,0.134496;2:0.652916,0.138164;5:231,0;\n" +
            "39506\n" +
            "2:0.656619,0.143462;2:0.645489,0.175688;5:232,0;\n" +
            "39538\n" +
            "2:0.612083,0.224541;2:0.579919,0.265449;5:233,0;\n" +
            "39571\n" +
            "2:0.556995,0.297077;2:0.544651,0.324121;5:234,0;\n" +
            "39603\n" +
            "2:0.541028,0.336834;2:0.540629,0.346232;5:235,0;\n" +
            "39637\n" +
            "2:0.545474,0.354950;2:0.563758,0.363877;5:236,0;\n" +
            "39672\n" +
            "2:0.582808,0.367741;2:0.603806,0.373567;5:237,0;\n" +
            "39705\n" +
            "2:0.616011,0.377543;2:0.624325,0.381774;5:238,0;\n" +
            "39736\n" +
            "2:0.628384,0.387035;2:0.624300,0.412356;5:239,0;\n" +
            "39770\n" +
            "2:0.595920,0.456427;2:0.561475,0.500197;5:240,0;\n" +
            "39802\n" +
            "2:0.538995,0.525687;2:0.519478,0.547171;5:241,0;\n" +
            "39833\n" +
            "2:0.511195,0.556106;3:0.511195,0.556106;5:242,0;\n";


}
