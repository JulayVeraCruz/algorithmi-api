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
public class UserCurse {
    private int user;
    private int curse;

    public UserCurse(int user, int curse) {
        this.user = user;
        this.curse = curse;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getCurse() {
        return curse;
    }

    public void setCurse(int curse) {
        this.curse = curse;
    }
    
    
}
