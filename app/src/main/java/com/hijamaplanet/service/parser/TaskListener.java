package com.hijamaplanet.service.parser;

import java.util.List;

public interface TaskListener {
    void onTaskFinish(List<List<String>> multiArrayList);
    void onTaskFinishSingleArray(List<String> SingleList);
    void onTaskFourthArray(List<List<String>> firstArrayList, List<List<String>> secondArrayList, List<List<String>> ThirdArrayList, List<List<String>> FourthArrayList);
}
