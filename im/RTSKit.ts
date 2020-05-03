/*
 * @Author: kangqiang
 * @Date: 2020/5/3 3:02 下午
 * @Last Modified by: kangqiang
 * @Last Modified time: 2020/5/3 3:02 下午
 */
import {NativeModules} from 'react-native';

const {RNNeteaseIm} = NativeModules;

export type RTSData = {
  sessionId: string; //会话ID
  account: string; //对方账号
  channelId: number; //数据通道
};

export type RtsCommandData = {
  roleType?: number; //角色
  currentBoardId?: number;
  type: number; //指令类型
  id?: number; //白板id
  url?: string; //链接
  paintColorType?: number; //画笔颜色类型
  pptIndex?: number; //课件页码
  seekTo?: number; //视频seek值
  startLessonTime?: number; //上课开始时间
  startLessonPrice?: number; //上课开始价格
};

const baseType: number = 14;

export const RtsCommandType = {
  AddBoard: baseType + 1,
  AddImage: baseType + 2,
  AddPpt: baseType + 3,
  AddVideo: baseType + 4,
  ChangeBoardPage: baseType + 5,
  DeleteBoard: baseType + 6,
  ChangePaintColor: baseType + 7,
  PptStartPlay: baseType + 8,
  PptEndPlay: baseType + 9,
  PptNextFrame: baseType + 10,
  PptPrevFrame: baseType + 11,
  PptChangePage: baseType + 12,
  VideoPlay: baseType + 13,
  VideoPause: baseType + 14,
  VideoSeek: baseType + 15,
  ImageRotate: baseType + 16,
  StartLesson: baseType + 17,
};

class RTSKit {
  /**
   * 注册/注销监听收到的会话请求
   *
   * @param register 注册/注销
   */
  registerRTSIncomingObserver(register: boolean) {
    return RNNeteaseIm.registerRTSIncomingObserver(register);
  }

  /**
   * /**
   * (发送方)发起会话， 调用此接口对方会收到相应的会话请求通知
   *
   * @param account 对方帐号
   * @param promise 返回RTSData
   */
  startNimSession(account: string): Promise<RTSData> {
    return RNNeteaseIm.startNimSession(account);
  }

  /**
   * 注册/注销发起会话后，被叫方的响应（接听、拒绝、忙）
   *
   * @param register 注册/注销
   */
  registerOutgoingObserver(register: boolean) {
    RNNeteaseIm.registerOutgoingObserver(register);
  }

  /**
   *  /**
   * 注册/注销同时在线的其他端对主叫方的响应
   *
   * @param register  注册/注销
   */
  registerInComingObserver(register: boolean, sessionId?: string) {
    RNNeteaseIm.registerInComingObserver(sessionId, register);
  }

  /**
   * /**
   * 注册/注销通道状态变化的通知
   * 注册/注销会话对方挂断的通知
   * 注册/注销到来的会话或者自己发起的会话（自己或者对方无响应）超时的通知，默认超时时间为40秒
   * 注册/注销会话控制消息
   *
   * @param register 注册/注销
   */
  registerCommonObserver(register: boolean) {
    RNNeteaseIm.registerCommonObserver(register);
  }

  /**
   * /**
   * (接收方)接受会话
   *
   * @param account 对方帐号
   * @param promise 返回本地通道初始化是否成功
   */
  acceptNimSession(account: string): Promise<boolean> {
    return RNNeteaseIm.acceptNimSession(account);
  }

  /**
   * (接受方)拒绝会话或者结束会话
   *
   * @param promise 是否成功调用
   */
  endNimSession(): Promise<boolean> {
    return RNNeteaseIm.endNimSession();
  }

  /**
   * 发送白板指令
   *
   * @param data ActionStep与所需参数
   */
  sendBoardCommand(data: RtsCommandData) {
    RNNeteaseIm.sendBoardCommand(data);
  }

  store(key: string, value: string) {
    RNNeteaseIm.store(key, value);
  }

  restore(key: string): Promise<string> {
    return RNNeteaseIm.restore(key);
  }
}

export default new RTSKit();
