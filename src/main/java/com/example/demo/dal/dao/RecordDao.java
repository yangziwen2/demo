package com.example.demo.dal.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.dal.model.Record;

@Repository
public interface RecordDao {

	public List<Record> getList();

}
