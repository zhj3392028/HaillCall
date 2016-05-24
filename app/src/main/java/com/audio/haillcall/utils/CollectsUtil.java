package com.audio.haillcall.utils;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: CollectsUtil
 * @Description: 集合工具类
 * @date 2015年5月26日 下午2:07:17
 */
public class CollectsUtil {

  /**
   * @param list 列表
   * @return true | false
   * @Title: isEmpty
   * @Description: 判断列表是否为空
   * @author ys
   * @date 2015年5月26日 上午11:36:32
   */
  public static <E> boolean isEmpty(List<E> list) {
    return list == null || list.size() == 0;
  }

  /**
   * @param arrays 数组
   * @return true | false
   * @Title: isEmpty
   * @Description: 判断数组是否为空
   * @author ys
   * @date 2015年5月26日 上午11:37:01
   */
  public static <E> boolean isEmpty(E[] arrays) {
    return arrays == null || arrays.length == 0;
  }

  /**
   * @param list 列表
   * @return true | false
   * @Title: isNotEmpty
   * @Description: 判断列表是否不为空
   * @author ys
   * @date 2015年5月26日 上午11:36:32
   */
  public static <E> boolean isNotEmpty(List<E> list) {
    return !isEmpty(list);
  }

  /**
   * @param arrays 数组
   * @return true | false
   * @Title: isNotEmpty
   * @Description: 判断数组是否不为空
   * @author ys
   * @date 2015年5月26日 上午11:37:01
   */
  public static <E> boolean isNotEmpty(E[] arrays) {
    return !isEmpty(arrays);
  }

  public static <E> List<E> asList(E[] arrays) {
    if (isEmpty(arrays)) {
      return null;
    }
    return Arrays.asList(arrays);
  }
}
