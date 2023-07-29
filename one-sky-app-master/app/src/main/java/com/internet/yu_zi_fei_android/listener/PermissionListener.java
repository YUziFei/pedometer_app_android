package com.internet.yu_zi_fei_android.listener;

import java.util.List;

/**
 * Created by Administrator on 2020/3/25.
 */

public interface PermissionListener {
    void granted();
    void denied(List<String> deniedList);
}
