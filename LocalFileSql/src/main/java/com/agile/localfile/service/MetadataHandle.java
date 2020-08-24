package com.agile.localfile.service;



import com.agile.localfile.pojo.Metadata;

import java.util.List;

/**
*
* @author 张睿
* @date 2020-08-07
* @param
* @return
*/

public interface MetadataHandle<T> {
    T handle (List<Metadata> metadataList);
}
