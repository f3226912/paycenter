package cn.gdeng.paycenter.util.server;


import java.util.Comparator;
import java.util.Date;

import cn.gdeng.paycenter.dto.right.BaseMenu;


public class MenuComparator implements Comparator<BaseMenu> {

    @Override
    public int compare(BaseMenu menu1, BaseMenu menu2) {
        int order = 0;
        order = menu2.getSort() - menu1.getSort();
        if (order == 0) {
            Date date2 = menu2.getUpdateTime();
            Date date1 = menu1.getUpdateTime();
            long d1 = date1 == null ? 0 : date1.getTime();
            long d2 = date2 == null ? 0 : date2.getTime();
            if (d2 > d1) {
                order = 1;
            } else if (d2 < d1) {
                order = -1;
            }
        }
        return order;
    }
}
