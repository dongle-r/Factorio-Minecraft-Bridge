data:extend({
    -- Send Chest
    {
        type = "container",
        name = "send-chest",
        icon = "__fmc-bridge__/graphics/send-chest-icon.png",
        icon_size = 32,
        flags = {"placeable-neutral", "player-creation"},
        minable = {mining_time = 1, result = "send-chest"},
        max_health = 100,
        corpse = "small-remnants",
        open_sound = { filename = "__base__/sound/metallic-chest-open.ogg", volume=0.65 },
        close_sound = { filename = "__base__/sound/metallic-chest-close.ogg", volume = 0.7 },
        collision_box = {{-0.35, -0.35}, {0.35, 0.35}},
        selection_box = {{-0.5, -0.5}, {0.5, 0.5}},
        fast_replaceable_group = "container",
        inventory_size = 1,
        vehicle_impact_sound =  { filename = "__base__/sound/car-metal-impact.ogg", volume = 0.65 },
        picture =
        {
            filename = "__fmc-bridge__/graphics/send-chest.png",
            priority = "extra-high",
            width = 48,
            height = 34,
            shift = {0.1875, 0}
        }
    },
    {
        type = "item",
        name = "send-chest",
        icon = "__fmc-bridge__/graphics/send-chest-icon.png",
        icon_size = 32,
        subgroup = "storage",
        order = "a[items]-b[send-chest]",
        place_result = "send-chest",
        stack_size = 50
    },
    {
        type = "recipe",
        name = "send-chest",
        ingredients = {{"wood", 2},{"iron-plate", 2}},
        result = "send-chest",
        energy_required = 0.25
    },
    -- Receive Chest
    {
        type = "container",
        name = "receive-chest",
        icon = "__fmc-bridge__/graphics/receive-chest-icon.png",
        icon_size = 32,
        flags = {"placeable-neutral", "player-creation"},
        minable = {mining_time = 1, result = "receive-chest"},
        max_health = 100,
        corpse = "small-remnants",
        open_sound = { filename = "__base__/sound/metallic-chest-open.ogg", volume=0.65 },
        close_sound = { filename = "__base__/sound/metallic-chest-close.ogg", volume = 0.7 },
        collision_box = {{-0.35, -0.35}, {0.35, 0.35}},
        selection_box = {{-0.5, -0.5}, {0.5, 0.5}},
        fast_replaceable_group = "container",
        inventory_size = 50,
        vehicle_impact_sound =  { filename = "__base__/sound/car-metal-impact.ogg", volume = 0.65 },
        picture =
        {
            filename = "__fmc-bridge__/graphics/receive-chest.png",
            priority = "extra-high",
            width = 48,
            height = 34,
            shift = {0.1875, 0}
        }
    },
    {
        type = "item",
        name = "receive-chest",
        icon = "__fmc-bridge__/graphics/receive-chest-icon.png",
        icon_size = 32,
        subgroup = "storage",
        order = "a[items]-b[receive-chest]",
        place_result = "receive-chest",
        stack_size = 50
    },
    {
        type = "recipe",
        name = "receive-chest",
        ingredients = {{"wood", 2},{"iron-plate", 2}},
        result = "receive-chest",
        energy_required = 0.25
    },
    -- Send Tank
    {
      type = "storage-tank",
      name = "send-tank",
      icons = {{icon="__base__/graphics/icons/storage-tank.png",tint = {r=.6,g=.6,b=1,a=1}}},
      icon_size = 64, icon_mipmaps = 4,
      flags = {"placeable-player", "player-creation"},
      minable = {mining_time = 0.5, result = "send-tank"},
      max_health = 500,
      corpse = "storage-tank-remnants",
      dying_explosion = "storage-tank-explosion",
      collision_box = {{-1.3, -1.3}, {1.3, 1.3}},
      selection_box = {{-1.5, -1.5}, {1.5, 1.5}},
      fluid_box =
      {
        base_area = 250,
        pipe_covers = pipecoverspictures(),
        pipe_connections =
        {
          { position = {-1, -2} },
          { position = {2, 1} },
          { position = {1, 2} },
          { position = {-2, -1} }
        }
      },
      two_direction_only = true,
      window_bounding_box = {{-0.125, 0.6875}, {0.1875, 1.1875}},
      pictures =
      {
        picture =
        {
          sheets =
          {
            {
              filename = "__base__/graphics/entity/storage-tank/storage-tank.png",
              priority = "extra-high",
              frames = 2,
              width = 110,
              height = 108,
              shift = util.by_pixel(0, 4),
              tint = {r=.6,g=.6,b=1,a=1},
              hr_version =
              {
                filename = "__base__/graphics/entity/storage-tank/hr-storage-tank.png",
                priority = "extra-high",
                frames = 2,
                width = 219,
                height = 215,
                shift = util.by_pixel(-0.25, 3.75),
                scale = 0.5,
                tint = {r=.6,g=.6,b=1,a=1},
              }
            },
            {
              filename = "__base__/graphics/entity/storage-tank/storage-tank-shadow.png",
              priority = "extra-high",
              frames = 2,
              width = 146,
              height = 77,
              shift = util.by_pixel(30, 22.5),
              draw_as_shadow = true,
              hr_version =
              {
                filename = "__base__/graphics/entity/storage-tank/hr-storage-tank-shadow.png",
                priority = "extra-high",
                frames = 2,
                width = 291,
                height = 153,
                shift = util.by_pixel(29.75, 22.25),
                scale = 0.5,
                draw_as_shadow = true
              }
            }
          }
        },
        fluid_background =
        {
          filename = "__base__/graphics/entity/storage-tank/fluid-background.png",
          priority = "extra-high",
          width = 32,
          height = 15
        },
        window_background =
        {
          filename = "__base__/graphics/entity/storage-tank/window-background.png",
          priority = "extra-high",
          width = 17,
          height = 24,
          hr_version =
          {
            filename = "__base__/graphics/entity/storage-tank/hr-window-background.png",
            priority = "extra-high",
            width = 34,
            height = 48,
            scale = 0.5
          }
        },
        flow_sprite =
        {
          filename = "__base__/graphics/entity/pipe/fluid-flow-low-temperature.png",
          priority = "extra-high",
          width = 160,
          height = 20
        },
        gas_flow =
        {
          filename = "__base__/graphics/entity/pipe/steam.png",
          priority = "extra-high",
          line_length = 10,
          width = 24,
          height = 15,
          frame_count = 60,
          axially_symmetrical = false,
          direction_count = 1,
          animation_speed = 0.25,
          hr_version =
          {
            filename = "__base__/graphics/entity/pipe/hr-steam.png",
            priority = "extra-high",
            line_length = 10,
            width = 48,
            height = 30,
            frame_count = 60,
            axially_symmetrical = false,
            animation_speed = 0.25,
            direction_count = 1,
            scale = 0.5
          }
        }
      },
      flow_length_in_ticks = 360,
      working_sound =
      {
        sound =
        {
            filename = "__base__/sound/storage-tank.ogg",
            volume = 0.6
        },
        match_volume_to_activity = true,
        audible_distance_modifier = 0.5,
        max_sounds_per_type = 3
      },
  
      circuit_wire_connection_points = circuit_connector_definitions["storage-tank"].points,
      circuit_connector_sprites = circuit_connector_definitions["storage-tank"].sprites,
      circuit_wire_max_distance = default_circuit_wire_max_distance,
      water_reflection =
      {
        pictures =
        {
          filename = "__base__/graphics/entity/storage-tank/storage-tank-reflection.png",
          priority = "extra-high",
          width = 24,
          height = 24,
          shift = util.by_pixel(5, 35),
          variation_count = 1,
          scale = 5,
        },
        rotate = false,
        orientation_to_variation = false
      }
    },
    {
        type = "item",
        name = "send-tank",
        icon = "__base__/graphics/icons/storage-tank.png",
        icon_size = 32,
        subgroup = "storage",
        order = "a[items]-b[send-tank]",
        place_result = "send-tank",
        stack_size = 10
    },
    {
        type = "recipe",
        name = "send-tank",
        ingredients = {{"iron-plate", 20}},
        result = "send-tank",
        energy_required = 0.25
    },
    -- Receive Tank
    {
      type = "storage-tank",
      name = "receive-tank",
      icons = {{icon="__base__/graphics/icons/storage-tank.png",tint = {r=1,g=.6,b=.6,a=1}}},
      icon_size = 64, icon_mipmaps = 4,
      flags = {"placeable-player", "player-creation"},
      minable = {mining_time = 0.5, result = "receive-tank"},
      max_health = 500,
      corpse = "storage-tank-remnants",
      dying_explosion = "storage-tank-explosion",
      collision_box = {{-1.3, -1.3}, {1.3, 1.3}},
      selection_box = {{-1.5, -1.5}, {1.5, 1.5}},
      fluid_box =
      {
        base_area = 250,
        pipe_covers = pipecoverspictures(),
        pipe_connections =
        {
          { position = {-1, -2} },
          { position = {2, 1} },
          { position = {1, 2} },
          { position = {-2, -1} }
        }
      },
      two_direction_only = true,
      window_bounding_box = {{-0.125, 0.6875}, {0.1875, 1.1875}},
      pictures =
      {
        picture =
        {
          sheets =
          {
            {
              filename = "__base__/graphics/entity/storage-tank/storage-tank.png",
              priority = "extra-high",
              frames = 2,
              width = 110,
              height = 108,
              shift = util.by_pixel(0, 4),
              tint = {r=1,g=.6,b=.6,a=1},
              hr_version =
              {
                filename = "__base__/graphics/entity/storage-tank/hr-storage-tank.png",
                priority = "extra-high",
                frames = 2,
                width = 219,
                height = 215,
                shift = util.by_pixel(-0.25, 3.75),
                scale = 0.5,
                tint = {r=1,g=.6,b=.6,a=1}
              }
            },
            {
              filename = "__base__/graphics/entity/storage-tank/storage-tank-shadow.png",
              priority = "extra-high",
              frames = 2,
              width = 146,
              height = 77,
              shift = util.by_pixel(30, 22.5),
              draw_as_shadow = true,
              hr_version =
              {
                filename = "__base__/graphics/entity/storage-tank/hr-storage-tank-shadow.png",
                priority = "extra-high",
                frames = 2,
                width = 291,
                height = 153,
                shift = util.by_pixel(29.75, 22.25),
                scale = 0.5,
                draw_as_shadow = true
              }
            }
          }
        },
        fluid_background =
        {
          filename = "__base__/graphics/entity/storage-tank/fluid-background.png",
          priority = "extra-high",
          width = 32,
          height = 15
        },
        window_background =
        {
          filename = "__base__/graphics/entity/storage-tank/window-background.png",
          priority = "extra-high",
          width = 17,
          height = 24,
          hr_version =
          {
            filename = "__base__/graphics/entity/storage-tank/hr-window-background.png",
            priority = "extra-high",
            width = 34,
            height = 48,
            scale = 0.5
          }
        },
        flow_sprite =
        {
          filename = "__base__/graphics/entity/pipe/fluid-flow-low-temperature.png",
          priority = "extra-high",
          width = 160,
          height = 20
        },
        gas_flow =
        {
          filename = "__base__/graphics/entity/pipe/steam.png",
          priority = "extra-high",
          line_length = 10,
          width = 24,
          height = 15,
          frame_count = 60,
          axially_symmetrical = false,
          direction_count = 1,
          animation_speed = 0.25,
          hr_version =
          {
            filename = "__base__/graphics/entity/pipe/hr-steam.png",
            priority = "extra-high",
            line_length = 10,
            width = 48,
            height = 30,
            frame_count = 60,
            axially_symmetrical = false,
            animation_speed = 0.25,
            direction_count = 1,
            scale = 0.5
          }
        }
      },
      flow_length_in_ticks = 360,
      working_sound =
      {
        sound =
        {
            filename = "__base__/sound/storage-tank.ogg",
            volume = 0.6
        },
        match_volume_to_activity = true,
        audible_distance_modifier = 0.5,
        max_sounds_per_type = 3
      },
  
      circuit_wire_connection_points = circuit_connector_definitions["storage-tank"].points,
      circuit_connector_sprites = circuit_connector_definitions["storage-tank"].sprites,
      circuit_wire_max_distance = default_circuit_wire_max_distance,
      water_reflection =
      {
        pictures =
        {
          filename = "__base__/graphics/entity/storage-tank/storage-tank-reflection.png",
          priority = "extra-high",
          width = 24,
          height = 24,
          shift = util.by_pixel(5, 35),
          variation_count = 1,
          scale = 5,
        },
        rotate = false,
        orientation_to_variation = false
      }
    },
    {
        type = "item",
        name = "receive-tank",
        icon = "__base__/graphics/icons/storage-tank.png",
        icon_size = 32,
        subgroup = "storage",
        order = "a[items]-b[receive-tank]",
        place_result = "receive-tank",
        stack_size = 10
    },
    {
        type = "recipe",
        name = "receive-tank",
        ingredients = {{"iron-plate", 20}},
        result = "receive-tank",
        energy_required = 0.25
    },
    -- Send Accumulator
    {
        type = "electric-energy-interface",
        name = "send-accumulator",
        icons = { {icon = "__base__/graphics/icons/accumulator.png", tint = {r=.6, g=.6, b=1, a=1} } },
        icon_size = 64, icon_mipmaps = 4,
        flags = {"placeable-neutral", "player-creation"},
        minable = {mining_time = 0.1, result = "send-accumulator"},
        max_health = 150,
        corpse = "medium-remnants",
        subgroup = "other",
        collision_box = {{-0.9, -0.9}, {0.9, 0.9}},
        selection_box = {{-1, -1}, {1, 1}},
        drawing_box = {{-1, -1.5}, {1, 1}},
        gui_mode = "all",
        allow_copy_paste = true,
        energy_source =
        {
          type = "electric",
          buffer_capacity = "1GJ",
          input_flow_limit = "1.1GW",
          usage_priority = "tertiary"
        },
    
        energy_production = "0kW",
        energy_usage = "0kW",
        -- also 'pictures' for 4-way sprite is available, or 'animation' resp. 'animations'
        picture = accumulator_picture( {r=.6, g=.6, b=1, a=1} ),
      },
      {
        type = "item",
        name = "send-accumulator",
        icon = "__base__/graphics/icons/accumulator.png",
        icon_size = 32,
        subgroup = "storage",
        order = "a[items]-b[send-accumulator]",
        place_result = "send-accumulator",
        stack_size = 10
    },
    {
        type = "recipe",
        name = "send-accumulator",
        ingredients = {{"iron-plate", 30}},
        result = "send-accumulator",
        energy_required = 0.25
    },
    -- Receive Accumulator
    {
        type = "electric-energy-interface",
        name = "receive-accumulator",
        icons = { {icon = "__base__/graphics/icons/accumulator.png", tint = {r=1, g=.6, b=.6, a=1} } },
        icon_size = 64, icon_mipmaps = 4,
        flags = {"placeable-neutral", "player-creation"},
        minable = {mining_time = 0.1, result = "receive-accumulator"},
        max_health = 150,
        corpse = "medium-remnants",
        subgroup = "other",
        collision_box = {{-0.9, -0.9}, {0.9, 0.9}},
        selection_box = {{-1, -1}, {1, 1}},
        drawing_box = {{-1, -1.5}, {1, 1}},
        gui_mode = "all",
        allow_copy_paste = true,
        energy_source =
        {
          type = "electric",
          buffer_capacity = "1MJ",
          output_flow_limit = "1.1MW",
          usage_priority = "tertiary"
        },
    
        energy_production = "0kW",
        energy_usage = "0kW",
        -- also 'pictures' for 4-way sprite is available, or 'animation' resp. 'animations'
        picture = accumulator_picture( {r=1, g=0.6, b=.6, a=1} ),
      },
      {
        type = "item",
        name = "receive-accumulator",
        icon = "__base__/graphics/icons/accumulator.png",
        icon_size = 32,
        subgroup = "storage",
        order = "a[items]-b[receive-accumulator]",
        place_result = "receive-accumulator",
        stack_size = 10
    },
    {
        type = "recipe",
        name = "receive-accumulator",
        ingredients = {{"iron-plate", 30}},
        result = "receive-accumulator",
        energy_required = 0.25
    },
})