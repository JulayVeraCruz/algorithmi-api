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
public class Curse {
    private int codCurse;
    private String name;
    private int scholl;


    public Curse(int CodCurse, String name, int scholl) {
        this.codCurse = codCurse;
        this.name = name;
        this.scholl = scholl;
    }
    public int getCodCurse() {
        return codCurse;
    }

    public void setCodCurse(int codCurse) {
        this.codCurse = codCurse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScholl() {
        return scholl;
    }

    public void setScholl(int scholl) {
        this.scholl = scholl;
    }
    
    
    
            
    
}
