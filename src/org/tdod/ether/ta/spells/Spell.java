/****************************************************************************
 * Ether Code base, version 1.0                                             *
 *==========================================================================*
 * Copyright (C) 2011 by Ron Kinney                                         *
 * All rights reserved.                                                     *
 *                                                                          *
 * This program is free software; you can redistribute it and/or modify     *
 * it under the terms of the GNU General Public License as published        *
 * by the Free Software Foundation; either version 2 of the License, or     *
 * (at your option) any later version.                                      *
 *                                                                          *
 * This program is distributed in the hope that it will be useful,          *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of           *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the            *
 * GNU General Public License for more details.                             *
 *                                                                          *
 * Redistribution and use in source and binary forms, with or without       *
 * modification, are permitted provided that the following conditions are   *
 * met:                                                                     *
 *                                                                          *
 * * Redistributions of source code must retain this copyright notice,      *
 *   this list of conditions and the following disclaimer.                  *
 * * Redistributions in binary form must reproduce this copyright notice    *
 *   this list of conditions and the following disclaimer in the            *
 *   documentation and/or other materials provided with the distribution.   *
 *                                                                          *
 *==========================================================================*
 * Ron Kinney (ronkinney@gmail.com)                                         *
 * Ether Homepage:   http://tdod.org/ether                                  *
 ****************************************************************************/

package org.tdod.ether.ta.spells;

import java.io.Serializable;

import org.tdod.ether.ta.player.enums.PlayerClass;
import org.tdod.ether.taimpl.spells.enums.CureCondition;
import org.tdod.ether.taimpl.spells.enums.ManaEffect;
import org.tdod.ether.taimpl.spells.enums.MiscTargetEffect;
import org.tdod.ether.taimpl.spells.enums.MiscTargetEffect2;
import org.tdod.ether.taimpl.spells.enums.PoisonTarget;
import org.tdod.ether.taimpl.spells.enums.SpellTarget;
import org.tdod.ether.taimpl.spells.enums.SpellType;
import org.tdod.ether.taimpl.spells.enums.StatModifier;

/**
 * This interface defines a single spell.
 * @author minex
 */
public interface Spell extends Serializable, Comparable<Spell> {

   /**
    * Gets the name of the spell.
    * @return The name of the spell.
    * @see setName
    */
   String getName();

   /**
    * Sets the name of this spell.
    * @param name The name of this spell.
    * @see getName
    */
   void setName(String name);

   /**
    * Gets the spell level.
    * @return the spell level.
    */
   int getLevel();

   /**
    * The message that will be displayed to the room.
    * @return the room message.
    * @see setMessage
    */
   String getMessage();

   /**
    * Sets the message that will be displayed to the room when this spell is cast.
    * @param message the room message.
    * @see getMessage
    */
   void setMessage(String message);

   /**
    * Gets the player class that can cast this spell.
    * @return the player class that can cast this spell.
    * @see setSpellClass
    */
   PlayerClass getSpellClass();

   /**
    * Sets the player class that can cast this spell.
    * @param spellClass the player class that can cast this spell.
    * @see getSpellClass
    */
   void setSpellClass(PlayerClass spellClass);

   /**
    * Gets the spell type.
    * @return the spell type.
    * @see setSpellType
    */
   SpellType getSpellType();

   /**
    * Sets the spell type.
    * @param spellType the spell type.
    * @see getSpellType
    */
   void setSpellType(SpellType spellType);

   /**
    * A number representing the minimum effect for this spell.  This number is generic and is used for many spells.
    * about any spell.
    * @return the minimum effect for this spell.
    * @see setMinSpellEffect
    */
   int getMinSpellEffect();

   /**
    * Sets the number representing the minimum effect for this spell.
    * This number is generic and is used for many spells.
    * about any spell.
    * @param minSpellEffect the minimum spell effect.
    * @see getMinSpellEffect
    */
   void setMinSpellEffect(int minSpellEffect);

   /**
    * A number representing the maximum effect for this spell.  This number is generic and is used for many spells.
    * @return the maximum effect for this spell.
    * @see setMaxSpellEffect
    */
   int getMaxSpellEffect();

   /**
    * Sets the number representing the maximum effect for this spell.
    * This number is generic and is used for many spells.
    * about any spell.
    * @param maxSpellEffect the minimum spell effect.
    * @see getMaxSpellEffect
    */
   void setMaxSpellEffect(int maxSpellEffect);

   /**
    * Gets the base price, in gold, that this spell will cost at a store.
    * @return the base price, in gold, that this spell will cost at a store.
    * @see setCost
    */
   int getCost();

   /**
    * Sets the monetary cost of this spell.
    * @param cost monetary cost the cost of this spell.
    * @see getCost
    */
   void setCost(int cost);

   /**
    * Gets the target for this spell.
    * @return the target for this spell.
    * @see setSpellTarget
    */
   SpellTarget getSpellTarget();

   /**
    * Sets the spell target.
    * @param spellTarget the spell target.
    * @see getSpellTarget
    */
   void setSpellTarget(SpellTarget spellTarget);

   /**
    * Gets the value that will be applied to the targets armor rating when this spell is cast.
    * @return the value that will be applied to the targets armor rating when this spell is cast.
    * @see setArmorModifier
    */
   int getArmorModifier();

   /**
    * Sets the value that will be applied to the targets armor rating when this spell is cast.
    * @param armorModifier the value that will be applied to the targets armor rating when this spell is cast.
    * @see getArmorModifier
    */
   void setArmorModifier(int armorModifier);

   /**
    * Gets the condition that this spell will cure.
    * @return the condition that this spell will cure.
    * @see setCureCondition
    */
   CureCondition getCureCondition();

   /**
    * Sets the condition that this spell will cure.
    * @param removeCondition the condition that this spell will cure.
    * @see getCureCondition
    */
   void setCureCondition(CureCondition removeCondition);

   /**
    * Gets the target type that will be poisoned.
    * @return the target type that will be poisoned.
    * @see setPoisonTarget
    */
   PoisonTarget getPoisonTarget();

   /**
    * Sets the target type that will be poisoned.
    * @param poisonTarget the target type that will be poisoned.
    * @see getPoisonTarget
    */
   void setPoisonTarget(PoisonTarget poisonTarget);

   /**
    * Gets the effect that this spell will have on the targets mana.
    * @return the effect that this spell will have on the targets mana.
    * @see setManaEffect
    */
   ManaEffect getManaEffect();

   /**
    * Sets the effect that this spell will have on the targets mana.
    * @param manaEffect the effect that this spell will have on the targets mana.
    * @see getManaEffect
    */
   void setManaEffect(ManaEffect manaEffect);

   /**
    * Gets the bonus/penalty that will be applied to the targets stats.
    * @return the bonus/penalty that will be applied to the targets stats.
    * @see setStatPenalty
    */
   StatModifier getStatPenalty();

   /**
    * Sets the penalty that will be applied to the targets stats.
    * @param statModifier the penalty that will be applied to the targets stats.
    * @see getStatPenalty
    */
   void setStatPenalty(StatModifier statModifier);

   /**
    * Gets the bonus that will be applied to the targets stats.
    * @return the bonus that will be applied to the targets stats.
    * @see setStatPenalty
    */
   StatModifier getStatBonus();

   /**
    * Sets the bonus that will be applied to the targets stats.
    * @param statModifier the bonus that will be applied to the targets stats.
    * @see getStatPenalty
    */
   void setStatBonus(StatModifier statModifier);

   /**
    * Gets the misc target effect.
    * @return the misc target effect.
    * @see setMiscTargetEffect
    */
   MiscTargetEffect getMiscTargetEffect();

   /**
    * Sets the misc target effect.
    * @param miscTargetEffect the misc target effect.
    * @see getMiscTargetEffect
    */
   void setMiscTargetEffect(MiscTargetEffect miscTargetEffect);

   /**
    * Gets the misc target effect #2.
    * @return the misc target effect #2.
    * @see setMiscTargetEffect2
    */
   MiscTargetEffect2 getMiscTargetEffect2();

   /**
    * Sets the misc target effect #2.
    * @param miscTargetEffect2 the misc target effect #2.
    * @see getMiscTargetEffect2
    */
   void setMiscTargetEffect2(MiscTargetEffect2 miscTargetEffect2);

   /**
    * Gets the mana cost of this spell.
    * @return the mana cost of this spell.
    */
   int getMana();

   /**
    * Sets the mana cost of this spell.
    * @param mana the mana cost of this spell.
    */
   void setMana(int mana);

   /**
    * Returns true if this spell scales with the players level, false otherwise.
    * @return true if this spell scales with the players level, false otherwise.
    */
   boolean scalesWithLevel();

   /**
    * Sets the scales with level value of this spell.
    * @param value the scales with level value of this spell.
    */
   void setScalesWithLevel(int value);
}
