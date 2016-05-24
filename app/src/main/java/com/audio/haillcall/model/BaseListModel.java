package com.audio.haillcall.model;

import com.audio.haillcall.utils.CollectsUtil;

import java.util.ArrayList;
import java.util.List;



public abstract class BaseListModel<E> {

  private List<E> items;

  public BaseListModel() {
    items = new ArrayList<E>();
  }

  /**
   * @return 返回id 或 empty .
   * @Title: getLastId
   * @Description: 获取列表最后一条数据的id
   * @author ys
   * @date 2015年5月28日 上午11:28:57
   */
  public abstract int getLastId();

  /**
   * @param item 数据实体 .
   * @Title: addTop
   * @Description: 加在第一个位置
   * @author ys
   * @date 2015年5月28日 上午11:32:48
   */
  public void addTop(E item) {
    if (item == null) {
      return;
    }
    if (items == null) {
      items = new ArrayList<E>();
    }
    if (size() == 0) {
      items.add(item);
    } else {
      items.add(0, item);
    }
  }

  /**
   * @param itemLst 数据实体 .
   * @Title: addTop
   * @Description: 加在第一个位置
   * @author ys
   * @date 2015年5月28日 上午11:32:48
   */
  public void addLTop(List<E> itemLst) {
    if (CollectsUtil.isEmpty(itemLst)) {
      return;
    }
    if (items == null) {
      items = new ArrayList<E>();
    }
    if (size() == 0) {
      items.addAll(itemLst);
    } else {
      items.addAll(0, itemLst);
    }
  }

  /**
   * @param item 数据实体
   * @Title: addLast
   * @Description: 加在最后一个位置
   * @author ys
   * @date 2015年5月28日 上午11:37:27
   */
  public void addLast(E item) {
    if (item == null) {
      return;
    }
    if (items == null) {
      items = new ArrayList<E>();
    }
    items.add(item);
  }

  /**
   * @param itemLst 数据实体 .
   * @Title: addTop
   * @Description: 加在最后位置
   * @author ys
   * @date 2015年5月28日 上午11:32:48
   */
  public void addLast(List<E> itemLst) {
    if (CollectsUtil.isEmpty(itemLst)) {
      return;
    }
    if (items == null) {
      items = new ArrayList<E>();
    }
    items.addAll(itemLst);
  }

  /**
   * @param item 移除项数据 .
   * @Title: remove
   * @Description: 移除一项
   * @author ys
   * @date 2015年5月28日 上午11:44:49
   */
  public void remove(E item) {
    if (CollectsUtil.isNotEmpty(items)) {
      items.remove(item);
    }
  }

  /**
   * @param itemLst 移除项数据 .
   * @Title: remove
   * @Description: 移除多项
   * @author ys
   * @date 2015年5月28日 上午11:44:49
   */
  public void remove(List<E> itemLst) {
    if (CollectsUtil.isNotEmpty(items)) {
      items.removeAll(itemLst);
    }

  }

  /**
   * @return 列表长度 .
   * @Title: size
   * @Description: 列表长度
   * @author ys
   * @date 2015年5月28日 上午11:32:28
   */
  public int size() {
    if (CollectsUtil.isEmpty(items)) {
      return 0;
    }
    return items.size();
  }

  public boolean isEmpty() {
    return CollectsUtil.isEmpty(items);
  }

  public List<E> getItems() {
    return items;
}

  public void setItems(List<E> items) {
    this.items = items;
  }
}
