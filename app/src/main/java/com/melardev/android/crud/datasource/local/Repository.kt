package com.melardev.android.crud.datasource.local

interface Repository<ID, T> {

    fun getAll(): List<T>

    fun getById(id: ID): T?

    fun count(): Long

    fun insert(t: T): Long

    fun delete(t: T?): Int

    fun deleteById(id: ID): Int

    fun deleteAll(): Boolean

}
