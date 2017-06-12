package me.yamakaja.rpgpets.api.entity;

import org.bukkit.entity.Player;

/**
 * Created by Yamakaja on 10.06.17.
 */
public interface Pet {

    /**
     * @return The {@link PetDescriptor} describing the pet
     */
    PetDescriptor getPetDescriptor();

}