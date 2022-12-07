package com.example.demo.mp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;


/**
 * @author apple
 */
@Getter
@Setter
@FieldNameConstants
@TableName("users")
public class User {

    @TableId(type = IdType.NONE)
    private Long id;

    private String ssn;

    private String email;

    private String mobile;

    private String address;

}
