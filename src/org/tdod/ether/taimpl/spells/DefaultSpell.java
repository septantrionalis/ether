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

package org.tdod.ether.taimpl.spells;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.player.enums.PlayerClass;
import org.tdod.ether.ta.spells.Spell;
import org.tdod.ether.taimpl.spells.enums.CureCondition;
import org.tdod.ether.taimpl.spells.enums.ManaEffect;
import org.tdod.ether.taimpl.spells.enums.MiscTargetEffect;
import org.tdod.ether.taimpl.spells.enums.MiscTargetEffect2;
import org.tdod.ether.taimpl.spells.enums.PoisonTarget;
import org.tdod.ether.taimpl.spells.enums.SpellTarget;
import org.tdod.ether.taimpl.spells.enums.SpellType;
import org.tdod.ether.taimpl.spells.enums.StatModifier;

/**
 * This class represents a single spell.  See doc/spells.txt for more information.
 * 
 * @author minex
 */
public class DefaultSpell implements Spell {

   private static final long serialVersionUID = 4681690059638941058L;

   private static Log _log = LogFactory.getLog(DefaultSpell.class);

   private String               _name;
   private String               _message;
   private int                  _mana;
   private PlayerClass          _spellClass = PlayerClass.INVALID;
   private SpellType            _spellType = SpellType.NONE;
   private boolean              _scalesWithLevel;
   private int                  _minSpellEffect;
   private int                  _maxSpellEffect;
   private int                  _cost;
   private SpellTarget          _spellTarget = SpellTarget.SPECIFIED;
   private int                  _armorModifier ;
   private CureCondition        _removeCondition = CureCondition.NONE;
   private PoisonTarget         _poisonTarget = PoisonTarget.NONE;
   private ManaEffect           _manaEffect = ManaEffect.NONE;
   private StatModifier         _statPenalty = StatModifier.NONE; 
   private StatModifier         _statBonus = StatModifier.NONE; 
   private MiscTargetEffect     _miscTargetEffect = MiscTargetEffect.NONE;
   private MiscTargetEffect2    _miscTargetEffect2 = MiscTargetEffect2.NONE;
   

   /**
    * Constructs a default spell.
    */
   public DefaultSpell() {
      
   }

   //**********
   // Methods overwritten from Spell.
   //**********

   /**
    * Gets the name of the spell.
    * 
    * @return The name of the spell.
    * @see setName
    */
   public String getName() {
      return _name;
   }

   /**
    * Sets the name of this spell.
    * 
    * @param name The name of this spell.
    * @see getName
    */
   public void setName(String name) {
      _name = name;
   }

   /**
    * Gets the spell level.
    * 
    * @return the spell level.
    */
   public int getLevel() {
      return (_mana * 2) - 1 ;
   }
   
   /**
    * The message that will be displayed to the room.
    * 
    * @return the room message.
    * @see setMessage
    */
   public String getMessage() {
      return _message;
   }

   /**
    * Sets the message that will be displayed to the room when this spell is cast.
    * 
    * @param message the room message.
    * @see getMessage
    */
   public void setMessage(String message) {
      _message = message;
   }

   /**
    * Gets the player class that can cast this spell.
    * 
    * @return the player class that can cast this spell.
    * @see setSpellClass
    */
   public PlayerClass getSpellClass() {
      return _spellClass;
   }

   /**
    * Sets the player class that can cast this spell.
    * 
    * @param spellClass the player class that can cast this spell.
    * @see getSpellClass
    */
   public void setSpellClass(PlayerClass spellClass) {
      _spellClass = spellClass;
   }

   /**
    * Gets the spell type.
    * 
    * @return the spell type.
    * @see setSpellType
    */
   public SpellType getSpellType() {
      return _spellType;
   }

   /**
    * Sets the spell type.
    * 
    * @param spellType the spell type.
    * @see getSpellType
    */
   public void setSpellType(SpellType spellType) {
      _spellType = spellType;
      
      if (_spellType.equals(SpellType.SORCEROR_ATTACK)) {
         _spellClass = PlayerClass.SORCEROR;
      } else if (_spellType.equals(SpellType.ACOLYTE_ATTACK)) {
         _spellClass = PlayerClass.ACOLYTE;
      } else if (_spellType.equals(SpellType.DRUID_ATTACK)) {
         _spellClass = PlayerClass.DRUID;
      } else if (_spellType.equals(SpellType.NECROLYTE_ATTACK)) {
         _spellClass = PlayerClass.NECROLYTE;
      } else if (_spellType.equals(SpellType.REMOVE_CONDITION)) {
         _spellClass = PlayerClass.ACOLYTE;
      } else if (_spellType.equals(SpellType.SUSTENANCE)) {
         _spellClass = PlayerClass.DRUID;
      } else if (_spellType.equals(SpellType.HEALING)) {
         _spellClass = PlayerClass.ACOLYTE;
      } else if (_spellType.equals(SpellType.REGENERATION)) {
         _spellClass = PlayerClass.DRUID;
      } else if (_spellType.equals(SpellType.SUMMONING)) {
         _spellClass = PlayerClass.SORCEROR;
      } else if (_spellType.equals(SpellType.INVISIBILITY)) {
         _spellClass = PlayerClass.SORCEROR;
      } else if (_spellType.equals(SpellType.STAT_MOD)) {
         _spellClass = PlayerClass.SORCEROR;
      } else if (_spellType.equals(SpellType.CHARM)) {
         _spellClass = PlayerClass.DRUID;
      } else {
         _log.error("Got unknown spell type " + spellType);
      }
   }

   /**
    * A number representing the minimum effect for this spell.  This number is generic and is used for many spells.
    * about any spell.
    * 
    * @return the minimum effect for this spell.
    * @see setMinSpellEffect
    */
   public int getMinSpellEffect() {
      return _minSpellEffect;
   }

   /**
    * Sets the number representing the minimum effect for this spell.  
    * This number is generic and is used for many spells.
    * about any spell.
    * 
    * @param minSpellEffect the minimum spell effect.
    * @see getMinSpellEffect
    */
   public void setMinSpellEffect(int minSpellEffect) {
      _minSpellEffect = minSpellEffect;
   }

   /**
    * A number representing the maximum effect for this spell.  This number is generic and is used for many spells.
    * 
    * @return the maximum effect for this spell.
    * @see setMaxSpellEffect
    */
   public int getMaxSpellEffect() {
      return _maxSpellEffect;
   }

   /**
    * Sets the number representing the maximum effect for this spell.  
    * This number is generic and is used for many spells.
    * about any spell.
    * 
    * @param maxSpellEffect the minimum spell effect.
    * @see getMaxSpellEffect
    */
   public void setMaxSpellEffect(int maxSpellEffect) {
      _maxSpellEffect = maxSpellEffect;
   }

   /**
    * Gets the base price, in gold, that this spell will cost at a store.
    * 
    * @return the base price, in gold, that this spell will cost at a store.
    * @see setCost
    */
   public int getCost() {
      return _cost;
   }

   /**
    * Sets the monetary cost of this spell.
    * 
    * @param monetary cost the cost of this spell.
    * @see getCost
    */
   public void setCost(int cost) {
      _cost = cost;
   }

   /**
    * Gets the target for this spell.
    * 
    * @return the target for this spell.
    * @see setSpellTarget
    */
   public SpellTarget getSpellTarget() {
      return _spellTarget;
   }

   /**
    * Sets the spell target.
    * 
    * @param spellTarget the spell target.
    * @see getSpellTarget
    */
   public void setSpellTarget(SpellTarget spellTarget) {
      _spellTarget = spellTarget;
   }

   /**
    * Gets the value that will be applied to the targets armor rating when this spell is cast.
    * 
    * @return the value that will be applied to the targets armor rating when this spell is cast.
    * @see setArmorModifier
    */
   public int getArmorModifier() {
      return _armorModifier;
   }

   /**
    * Sets the value that will be applied to the targets armor rating when this spell is cast.
    * 
    * @param armorModifier the value that will be applied to the targets armor rating when this spell is cast.
    * @see getArmorModifier
    */
   public void setArmorModifier(int armorModifier) {
      _armorModifier = armorModifier;
   }

   /**
    * Gets the condition that this spell will cure.
    * 
    * @return the condition that this spell will cure.
    * @see setRemoveCondition
    */
   public CureCondition getCureCondition() {
      return _removeCondition;
   }

   /**
    * Sets the condition that this spell will cure.
    * 
    * @param removeCondition the condition that this spell will cure.
    * @see getRemoveCondition
    */
   public void setCureCondition(CureCondition removeCondition) {
      _removeCondition = removeCondition;
   }

   /**
    * Gets the target type that will be poisoned.
    * 
    * @return the target type that will be poisoned.
    * @see setPoisonTarget
    */
   public PoisonTarget getPoisonTarget() {
      return _poisonTarget;
   }

   /**
    * Sets the target type that will be poisoned.
    * 
    * @param poisonTarget the target type that will be poisoned.
    * @see getPoisonTarget
    */
   public void setPoisonTarget(PoisonTarget poisonTarget) {
      _poisonTarget = poisonTarget;
   }

   /**
    * Gets the effect that this spell will have on the targets mana.
    * 
    * @return the effect that this spell will have on the targets mana.
    * @see setManaEffect
    */
   public ManaEffect getManaEffect() {
      return _manaEffect;
   }

   /**
    * Sets the effect that this spell will have on the targets mana.
    * 
    * @param manaEffect the effect that this spell will have on the targets mana.
    * @see getManaEffect
    */
   public void setManaEffect(ManaEffect manaEffect) {
      _manaEffect = manaEffect;
   }

   /**
    * Gets the penalty that will be applied to the targets stats.
    * 
    * @return the penalty that will be applied to the targets stats.
    * @see setStatModifier
    */
   public StatModifier getStatPenalty() {
      return _statPenalty;
   }

   /**
    * Sets the penalty that will be applied to the targets stats.
    * 
    * @param statModifier the penalty that will be applied to the targets stats.
    * @see getStatModifier
    */
   public void setStatPenalty(StatModifier statModifier) {
      _statPenalty = statModifier;
   }

   /**
    * Gets the bonus that will be applied to the targets stats.
    * 
    * @return the bonus that will be applied to the targets stats.
    * @see setStatPenalty
    */
   public StatModifier getStatBonus() {
      return _statBonus;
   }

   /**
    * Sets the bonus that will be applied to the targets stats.
    * 
    * @param statModifier the bonus that will be applied to the targets stats.
    * @see getStatPenalty
    */
   public void setStatBonus(StatModifier statModifier) {
      _statBonus = statModifier;
   }
   
   /**
    * Gets the misc target effect.
    * 
    * @return the misc target effect.
    * @see setMiscTargetEffect
    */
   public MiscTargetEffect getMiscTargetEffect() {
      return _miscTargetEffect;
   }

   /**
    * Sets the misc target effect.
    * 
    * @param miscTargetEffect the misc target effect.
    * @see getMiscTargetEffect
    */
   public void setMiscTargetEffect(MiscTargetEffect miscTargetEffect) {
      _miscTargetEffect = miscTargetEffect;
   }

   /**
    * Gets the misc target effect #2.
    * 
    * @return the misc target effect #2.
    * @see setMiscTargetEffect2
    */
   public MiscTargetEffect2 getMiscTargetEffect2() {
      return _miscTargetEffect2;
   }

   /**
    * Sets the misc target effect #2.
    * 
    * @param miscTargetEffect3 the misc target effect #2.
    * @see getMiscTargetEffect2
    */
   public void setMiscTargetEffect2(MiscTargetEffect2 miscTargetEffect2) {
      _miscTargetEffect2 = miscTargetEffect2;
   }

   /**
    * Gets the mana cost of this spell.
    * 
    * @return the mana cost of this spell.
    */
   public int getMana() {
      return _mana;
   }

   /**
    * Sets the mana cost of this spell.
    * 
    * @param mana the mana cost of this spell.
    */
   public void setMana(int mana) {
      _mana = mana;
   }
   
   /**
    * Returns true if this spell scales with the players level, false otherwise.
    * 
    * @return true if this spell scales with the players level, false otherwise.
    */
   public boolean scalesWithLevel() {
      return _scalesWithLevel;
   }
   
   /**
    * Sets the scales with level value of this spell.
    * 
    * @param value the scales with level value of this spell.
    */
   public void setScalesWithLevel(int value) {
      if (value == 0) {
         _scalesWithLevel = false ;
      } else if (value == 1) {
         _scalesWithLevel = true ;
      } else {
         _log.error(_name + " has a non-boolean value for the level scaling field: " + value) ;
      }
      
   }

   public String toString() {
      return _name;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof DefaultSpell)) {
         return false ;
      }
      if (obj == this) {
         return true ;
      }
      DefaultSpell otherObject = (DefaultSpell)obj ;

      if (otherObject.getName().equals(_name)) {
         return true;
      }
      
      return false;
   }
   
   public int hashCode() {
      int result = 17 ;
      result = 37*result + _name.hashCode();
      result = 37*result + _message.hashCode();
      result = 37*result + _mana;
      result = 37*result + _minSpellEffect;
      result = 37*result + _maxSpellEffect;
      result = 37*result + _cost;
      result = 37*result + _armorModifier;
      return result ;

   }

   //**********
   // Methods inherited from Comparable.
   //**********

   /**
    * Compares this spell to another spell.
    * @param o1 the other object.
    * @return -1, 0, or 1
    */
   public int compareTo(Spell o1) {
      if (this.getCost() == o1.getCost()) {
         return 0;
      } else if (this.getCost() > o1.getCost()) {
         return 1;
      } else {
         return -1;
      }
   }

}
