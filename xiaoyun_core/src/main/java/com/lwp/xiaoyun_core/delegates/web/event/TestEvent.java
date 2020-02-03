package com.lwp.xiaoyun_core.delegates.web.event;

import android.widget.Toast;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/2/4 1:27
 *     desc   :
 * </pre>
 */
public class TestEvent extends Event {

    @Override
    public String execute(String params) {
        Toast.makeText(getContext(), params, Toast.LENGTH_LONG).show();
        return null;
    }
}
