/*
Copyright 2016-2018 Jan Henzel, Patrick Jauernig, Dennis Werner

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package org.secuso.privacyfriendlywifimanager.logic.types;

import java.util.Comparator;
import java.util.TreeSet;

public class PrimitiveCellInfoTreeSet extends TreeSet<PrimitiveCellInfo> {
    private static final long serialVersionUID = 8437449073091988702L;

    public PrimitiveCellInfoTreeSet() {
        super(new Comparator<PrimitiveCellInfo>() {
            @Override
            public int compare(PrimitiveCellInfo lhs, PrimitiveCellInfo rhs) {
                //      return > 0 if rhs should be before lhs
                //      return < 0 if lhs should be before rhs
                //      return 0 otherwise
                return lhs.equals(rhs) ? 0 : (int) (rhs.getSignalStrength() - lhs.getSignalStrength());
            }
        });
    }
}
