const HOOK_ENGINES = {
  fridaGum: {
    id: 'frida-gum',
    label: 'Frida-Gum',
    strengths: {
      complexity: 10,
      performance: 5,
      stability: 7,
      startupCost: 4,
    },
    notes: 'Most powerful for complex and dynamic hooks, but heavier at runtime.',
  },
  and64InlineHook: {
    id: 'and64inlinehook',
    label: 'And64InlineHook',
    strengths: {
      complexity: 6,
      performance: 10,
      stability: 6,
      startupCost: 9,
    },
    notes: 'Very lightweight and fast, ideal for performance-critical and low-overhead hooks.',
  },
  shadowHook: {
    id: 'shadowhook',
    label: 'ShadowHook',
    strengths: {
      complexity: 7,
      performance: 8,
      stability: 10,
      startupCost: 8,
    },
    notes: 'Production-focused stability with strong compatibility for Android app environments.',
  },
};

const DEFAULT_PRIORITIES = {
  complexity: 0.2,
  performance: 0.3,
  stability: 0.4,
  startupCost: 0.1,
};

function normalizePriorities(priorities = DEFAULT_PRIORITIES) {
  const total = Object.values(priorities).reduce((sum, value) => sum + value, 0);

  if (total <= 0) {
    return { ...DEFAULT_PRIORITIES };
  }

  return Object.fromEntries(
    Object.entries(priorities).map(([key, value]) => [key, value / total]),
  );
}

function scoreEngine(engine, priorities) {
  return Object.entries(priorities).reduce(
    (sum, [metric, weight]) => sum + (engine.strengths[metric] ?? 0) * weight,
    0,
  );
}

function chooseHookEngine(options = {}) {
  const {
    preferredEngine,
    workload = 'balanced',
    priorities,
  } = options;

  if (preferredEngine) {
    const selected = Object.values(HOOK_ENGINES).find(
      (engine) => engine.id === preferredEngine,
    );

    if (!selected) {
      throw new Error(`Unknown preferred engine: ${preferredEngine}`);
    }

    return {
      engine: selected,
      reason: 'Developer explicitly selected the hook engine backend.',
      scores: null,
    };
  }

  const workloadPriorities = {
    balanced: DEFAULT_PRIORITIES,
    heavyInstrumentation: {
      complexity: 0.45,
      performance: 0.15,
      stability: 0.3,
      startupCost: 0.1,
    },
    performanceCritical: {
      complexity: 0.1,
      performance: 0.55,
      stability: 0.25,
      startupCost: 0.1,
    },
    productionSafe: {
      complexity: 0.1,
      performance: 0.25,
      stability: 0.55,
      startupCost: 0.1,
    },
  };

  const chosenPriorities = normalizePriorities(
    priorities ?? workloadPriorities[workload] ?? DEFAULT_PRIORITIES,
  );

  const scores = Object.values(HOOK_ENGINES)
    .map((engine) => ({
      engine,
      score: scoreEngine(engine, chosenPriorities),
    }))
    .sort((a, b) => b.score - a.score);

  const [winner] = scores;

  return {
    engine: winner.engine,
    reason: `Auto-selected from workload profile "${workload}" using weighted metrics.`,
    scores: scores.map(({ engine, score }) => ({ id: engine.id, score })),
    priorities: chosenPriorities,
  };
}

module.exports = {
  HOOK_ENGINES,
  DEFAULT_PRIORITIES,
  chooseHookEngine,
};
