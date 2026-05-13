export const Colors = {
  primary: '#1E40AF',
  primaryLight: '#DBEAFE',
  primaryDark: '#1E3A8A',
  primaryGradient: ['#2563EB', '#1D4ED8'],

  secondary: '#0E7490',
  secondaryLight: '#CFFAFE',
  secondaryDark: '#155E75',

  accent: '#D97706',
  accentLight: '#FEF3C7',
  accentDark: '#B45309',

  success: '#059669',
  successLight: '#D1FAE5',
  successDark: '#047857',

  warning: '#D97706',
  warningLight: '#FEF3C7',

  error: '#DC2626',
  errorLight: '#FEE2E2',

  background: '#F8FAFC',
  surface: '#FFFFFF',
  surfaceAlt: '#F1F5F9',

  text: '#0F172A',
  textSecondary: '#475569',
  textMuted: '#94A3B8',
  textInverse: '#FFFFFF',

  border: '#E2E8F0',
  borderLight: '#F1F5F9',

  scienceColor: '#2563EB',
  scienceLight: '#DBEAFE',
  scienceGradient: ['#3B82F6', '#2563EB'],

  mathColor: '#7C3AED',
  mathLight: '#EDE9FE',
  mathGradient: ['#8B5CF6', '#7C3AED'],

  commerceColor: '#059669',
  commerceLight: '#D1FAE5',
  commerceGradient: ['#10B981', '#059669'],

  cardShadow: 'rgba(15, 23, 42, 0.08)',
  cardShadowHeavy: 'rgba(15, 23, 42, 0.12)',

  gradientHero: ['#1E40AF', '#3B82F6'],
  gradientWarm: ['#F59E0B', '#EF4444'],
  gradientCool: ['#06B6D4', '#2563EB'],
  gradientSuccess: ['#059669', '#10B981'],
};

export function getSubjectColor(subject: string): string {
  switch (subject) {
    case 'Science': return Colors.scienceColor;
    case 'Mathematics': return Colors.mathColor;
    case 'Commerce': return Colors.commerceColor;
    default: return Colors.primary;
  }
}

export function getSubjectLight(subject: string): string {
  switch (subject) {
    case 'Science': return Colors.scienceLight;
    case 'Mathematics': return Colors.mathLight;
    case 'Commerce': return Colors.commerceLight;
    default: return Colors.primaryLight;
  }
}

export type GradientTuple = readonly [string, string, ...string[]];

export function getSubjectGradient(subject: string): GradientTuple {
  switch (subject) {
    case 'Science': return Colors.scienceGradient as unknown as GradientTuple;
    case 'Mathematics': return Colors.mathGradient as unknown as GradientTuple;
    case 'Commerce': return Colors.commerceGradient as unknown as GradientTuple;
    default: return Colors.gradientHero as unknown as GradientTuple;
  }
}

export function asGradient(colors: string[]): GradientTuple {
  return colors as unknown as GradientTuple;
}
