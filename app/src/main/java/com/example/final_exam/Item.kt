package com.example.final_exam

import java.io.Serializable

data class Item(var name: String ?=null, var description: String ?= null, var price: Long ?= null, var imageUrl: String? = null) : Serializable


