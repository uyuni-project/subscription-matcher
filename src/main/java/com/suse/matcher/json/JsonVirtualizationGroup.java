package com.suse.matcher.json;

import java.util.Set;

/**
 * JSON representation of a group of virtual guests which belong to the same
 * cloud, VMWare vCenter, etc.
 */
public class JsonVirtualizationGroup {

    private Long id;
    private String name;
    private String type;
    private Set<Long> virtualGuestIds;

    /**
     * Standard constructor.
     * @param idIn an identifier, unique for a given type
     * @param nameIn a descriptive name
     * @param typeIn a type label
     * @param virtualGuestIdsIn set of ids of Virtual guests in this group
     */
    public JsonVirtualizationGroup(Long idIn, String nameIn, String typeIn,
            Set<Long> virtualGuestIdsIn) {
        id = idIn;
        name = nameIn;
        type = typeIn;
        virtualGuestIds = virtualGuestIdsIn;
    }

    /**
     * Gets the id.
     *
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param idIn the new id
     */
    public void setId(Long idIn) {
        id = idIn;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param nameIn the new name
     */
    public void setName(String nameIn) {
        name = nameIn;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param typeIn the new type
     */
    public void setType(String typeIn) {
        type = typeIn;
    }

    /**
     * Gets the set of ids of Virtual guests in this group.
     *
     * @return set of ids of Virtual guests
     */
    public Set<Long> getVirtualGuestIds() {
        return virtualGuestIds;
    }

    /**
     * Sets the ids of Virtual guests in this group.
     *
     * @param virtualGuestIdsIn the new set of ids of Virtual guests
     */
    public void setVirtualGuestIds(Set<Long> virtualGuestIdsIn) {
        virtualGuestIds = virtualGuestIdsIn;
    }
}
