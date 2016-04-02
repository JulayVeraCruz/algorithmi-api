/*
 * Copyright 2016 Pedro Dias.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package algorithmi.models;

import java.util.Date;

/**
 *
 * @author Pedro Dias
 */
public class User {

    private int id_User;
    private String name;
    private String password;
    private Date dateBirth;
    private String email;
    private int type;

    public User(int id_User, String name, String password, Date dateBirth, String email, int type) {
        this.id_User = id_User;
        this.name = name;
        this.password = password;
        this.dateBirth = dateBirth;
        this.email = email;
        this.type = type;
    }

//
//    @Override
//    public int hashCode() {
//        int hash = 7;
//        hash = 83 * hash + Objects.hashCode(this.name);
//        hash = 83 * hash + Objects.hashCode(this.password);
//        return hash;
//    }
    public int getId_User() {
        return id_User;
    }

    public void setId_User(int id_User) {
        this.id_User = id_User;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(Date dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
