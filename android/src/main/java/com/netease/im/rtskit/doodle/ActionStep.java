package com.netease.im.rtskit.doodle;

public class ActionStep {
    //export enum WBCommand {
//  start = 1,
//  move = 2,
//  end = 3,
//  undo = 4,
//  package = 5,
//  clear = 6,
//  clearResponse = 7,
//  syncRequest = 8,
//  sync = 9,
//  syncReady = 10,
//  syncReadyResponse = 11,
//  mark = 12,
//  markEnd = 13,
//  docShare = 14,
//
//  addBoard = 21,
//  addDoc = 22,
//  addPhoto = 23,
//  penColor = 24,
//  eraser = 25,
//  deleteBoard = 26,
//  addVideo = 27
//}
    static final byte BASE = 14;
    static final byte START = 1;
    static final byte MOVE = 2;
    static final byte END = 3;
    static final byte REVOKE = 4;
    static final byte ERASER = 6;
    static final byte CLEAR_SELF = 7;
    static final byte CLEAR_ACK = 8;
    public static final byte ADD_BOARD = BASE + 1;
    public static final byte ADD_IMAGE = BASE + 2;
    public static final byte ADD_PPT = BASE + 3;
    public static final byte ADD_VIDEO = BASE + 4;
    public static final byte CHANGE_BOARD = BASE + 5;
    public static final byte DELETE_BOARD = BASE + 6;
    public static final byte CHANGE_PAINT_COLOR = BASE + 7;
    public static final byte PPT_START_PLAY = BASE + 8;
    public static final byte PPT_END_PLAY = BASE + 9;
    public static final byte PPT_NEXT_FRAME = BASE + 10;
    public static final byte PPT_PREV_FRAME = BASE + 11;
    public static final byte PPT_CHANGE_PAGE = BASE + 12;
    public static final byte VIDEO_PLAY = BASE + 13;
    public static final byte VIDEO_PAUSE = BASE + 14;
    public static final byte VIDEO_SEEK = BASE + 15;
    public static final byte ImageRotate = BASE + 16;
    public static final byte START_LESSON = BASE + 17;
    public static final byte START_TRAIL = BASE + 18;
    public static final byte MODIFY_PRICE = BASE + 19;
    public static final byte END_LESSON = BASE + 20;
//     AddBoard: baseType + 1,
//  ChangeBoardPage: baseType + 5,
//  DeleteBoard: baseType + 6,
//  ChangePaintColor: baseType + 7,
//  PptStartPlay: baseType + 8,
//  PptEndPlay: baseType + 9,
//  PptNextFrame: baseType + 10,
//  PptPrevFrame: baseType + 11,
//  PptChangePage: baseType + 12,
//  VideoPlay: baseType + 13,
//  VideoPause: baseType + 14,
//  VideoSeek: baseType + 15,
//  ImageRotate: baseType + 16,
}
