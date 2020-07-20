package com.yzf.esdemo.repository;

import com.yzf.esdemo.dto.VoucherDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User:zhaozhihui
 * Date: 2020/07/17
 * Time: 10:56 上午
 */

@Repository
public interface ElVoucherRepository extends ElasticsearchRepository<VoucherDTO, Long> {
}
