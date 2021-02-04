package com.make.cameracopy;

import android.hardware.Camera;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class CameraSize {
    public static Camera.Size a(List<Camera.Size> paramList, int paramInt, Camera.Size paramSize) {
        if (paramList == null || paramList.isEmpty())
            return paramSize;
        Collections.sort(paramList, new a());
        byte b = 0;
        Iterator<Camera.Size> iterator = paramList.iterator();
        while (iterator.hasNext() && ((Camera.Size)iterator.next()).width <= paramInt)
            b++;
        return (b == paramList.size()) ? paramList.get(b - 1) : paramList.get(b);
    }

    public static final class a implements Comparator<Camera.Size> {
        public int compare(Camera.Size param1Object1, Camera.Size param1Object2) {
            int i = ((Camera.Size)param1Object1).width;
            int j = ((Camera.Size)param1Object2).width;
            if (i == j) {
                i = 0;
            } else if (i > j) {
                i = 1;
            } else {
                i = -1;
            }
            return i;
        }
    }
}