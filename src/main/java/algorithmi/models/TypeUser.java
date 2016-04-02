/*
 * Copyright 2016 FilipeRosa.
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

/**
 *
 * @author FilipeRosa
 */
public class TypeUser {
    private int id_Type;
    private String name;

    public TypeUser(int id_Type, String name) {
        this.id_Type = id_Type;
        this.name = name;
    }

    public int getId_Type() {
        return id_Type;
    }

    public void setId_Type(int id_Type) {
        this.id_Type = id_Type;
    }

    public String getName() {
        return name;
    }

    public void setName(String Name) {
        this.name = Name;
    }
    
}
