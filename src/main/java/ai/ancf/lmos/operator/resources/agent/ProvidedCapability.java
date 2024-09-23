/*
 * SPDX-FileCopyrightText: 2024 Deutsche Telekom AG
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package ai.ancf.lmos.operator.resources.agent;

import io.fabric8.generator.annotation.Required;

import java.util.Objects;

public class ProvidedCapability extends Capability {
    @Required
    private String description;

    public ProvidedCapability() {
    }

    public ProvidedCapability(String name, String version, String description) {
        super(name, version);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ProvidedCapability that = (ProvidedCapability) o;
        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), description);
    }
}